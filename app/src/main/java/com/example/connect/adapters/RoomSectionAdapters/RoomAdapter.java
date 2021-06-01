package com.example.connect.adapters.RoomSectionAdapters;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.connect.AuthenticationActivities.Events.OpenRoomEvent;
import com.example.connect.AuthenticationActivities.WebSocketService;
import com.example.connect.Entities.Room;
import com.example.connect.RoomActivity;
import com.example.connect.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;


public final class RoomAdapter extends Adapter {

    private final Context c;
    private ArrayList<Room> roomList;
    WebSocketService wss = WebSocketService.getWebSocketService();
    public final HashMap<String,Integer> editedRoomsHashMap = new HashMap<>();
    public final HashMap<String,Integer> deletedRoomsHashMap = new HashMap<>();

    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(v);
    }

    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = this.roomList.get(position);
        holder.getTitle().setText(room.getName());
        holder.getDesc().setText(room.getDescription());
        holder.getTile().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(holder.getV().getContext(), RoomActivity.class);
                holder.getV().getContext().startActivity(intent);
                EventBus.getDefault().post(new OpenRoomEvent(room));
            }
        });
    }


    public void onBindViewHolder(@NonNull ViewHolder vh, int i) {
        this.onBindViewHolder((RoomViewHolder)vh, i);
    }

    public int getItemCount() {
        Log.d("get Item count  ", ""+this.roomList.size());
        return this.roomList.size();
    }

    public void setFilter(ArrayList<Room> roomModels){
        roomList = new ArrayList<>();
        roomList.addAll(roomModels);
        notifyDataSetChanged();
    }

    public final void removeItemFromList(int position){
        this.roomList.remove(position);
    }

    public final void replaceItemFromList(int postion,Room room){
        this.roomList.remove(postion);
        roomList.add(postion,room);

    }

    public final ArrayList<Room> getRoomList() {
        return this.roomList;
    }

    public RoomAdapter(Context c, ArrayList<Room> roomList) {
        super();
        this.c = c;
        this.roomList = roomList;
    }



    public final class RoomViewHolder extends ViewHolder {

        private TextView name;
        private TextView detail;
        private ImageView mMenus;
        private LinearLayout tile;
        private final View v;

        public final TextView getTitle() {
            return this.name;
        }
        public final TextView getDesc() {
            return this.detail;
        }

        private void popupMenus(View v) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            Object obj = RoomAdapter.this.getRoomList().get(this.getAdapterPosition());
            final Room selectedRoom = (Room)obj;
            PopupMenu popupMenus = new PopupMenu(getV().getContext(), v);

            popupMenus.inflate(R.menu.edit_delete_room_menu);
            popupMenus.setOnMenuItemClickListener(it -> {
                boolean bool;
                switch(it.getItemId()) {
                    case R.id.editText:
                        View view = LayoutInflater.from(getV().getContext()).inflate(R.layout.add_room_item, null);
                        final EditText roomName = view.findViewById(R.id.roomName);
                        final EditText roomDetail = view.findViewById(R.id.roomDetails);
                        roomName.setText(selectedRoom.getName());
                        roomDetail.setText(selectedRoom.getDescription());
                        (new Builder(getV().getContext())).setView(view).setPositiveButton("Ok", (dialog, $noName_1) -> {
                            String name = roomName.getText().toString();
                            String details = roomDetail.getText().toString();
                            if(name.equals("") || details.equals("")){
                                Toast.makeText(getV().getContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                editedRoomsHashMap.put(selectedRoom.getRid(),this.getAdapterPosition());
                                JSONObject json = new JSONObject();
                                try {
                                    json.put("name",name);
                                    json.put("description", details);
                                    json.put("id",""+selectedRoom.getId());
                                    json.put("rid",selectedRoom.getRid());
                                    json.put("createdBy",selectedRoom.getCreatedByUser());
                                    wss.fireDataToServer(WebSocketService.EDIT_ROOM,json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            dialog.dismiss();
                        }).setNegativeButton("Cancel", null).create().show();
                        bool = true;
                        break;
                    case R.id.delete:
                        (new Builder(getV().getContext())).setTitle("Delete").setIcon(R.drawable.ic_warning).setMessage("Are you sure delete this Room").setPositiveButton("Yes", (dialog, $noName_1) -> {
                            deletedRoomsHashMap.put(selectedRoom.getRid(),this.getAdapterPosition());
                            Log.d("Positon for "+selectedRoom.getRid(),""+this.getAdapterPosition());
                            JSONObject json = new JSONObject();
                            try {
                                json.put("id",""+selectedRoom.getId());
                                json.put("rid",selectedRoom.getRid());
                                json.put("name",selectedRoom.getName());
                                json.put("description", selectedRoom.getDescription());
                                wss.fireDataToServer(WebSocketService.DELETE_ROOM,json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }).setNegativeButton("No", null).create().show();
                        bool = true;
                        break;
                    default:
                        bool = true;
                }
                return bool;
            });
            popupMenus.show();
            Field popup = PopupMenu.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            Object menu = popup.get(popupMenus);
            assert menu != null;
            menu.getClass().getDeclaredMethod("setForceShowIcon", Boolean.TYPE).invoke(menu, true);
        }


        public final View getV() {
            return this.v;
        }

        public final View getTile() {return this.tile;}

        public RoomViewHolder(View v) {
            super(v);
            this.v = v;
            View view = this.v.findViewById(R.id.roomTitle);
            this.name = (TextView)view;
            view = this.v.findViewById(R.id.roomDetail);
            this.detail = (TextView)view;
            view = this.v.findViewById(R.id.mMenus);
            this.mMenus = (ImageView)view;
            view = this.v.findViewById(R.id.roomTile);
            this.tile = (LinearLayout)view;
            this.mMenus.setOnClickListener(it -> {
                RoomViewHolder viewHolder = RoomViewHolder.this;
                try {
                    viewHolder.popupMenus(it);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}


