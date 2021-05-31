package com.example.connect.adapters.RoomSectionAdapters.MemberAdapter;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.connect.model.Member;
import com.example.connect.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public final class MemberAdapter extends Adapter {

    private final Context c;
    private ArrayList<Member> memberList;

    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.member_item, parent, false);

        ImageView imageView = v.findViewById(R.id.memImage);
        String url = "https://picsum.photos/200";
        Picasso.get().load(url).into(imageView);
        return new MemberViewHolder(v);
    }

    public void onBindViewHolder(MemberViewHolder holder, int position) {
        Object obj = this.memberList.get(position);
        Member newList = (Member)obj;
        holder.getName().setText(newList.getMemberName());
        holder.getEmail().setText(newList.getMemberEmail());
    }

    public void onBindViewHolder(@NonNull ViewHolder vh, int i) {
        this.onBindViewHolder((MemberViewHolder)vh, i);
    }

    public int getItemCount() {
        return this.memberList.size();
    }

    public void setFilter(ArrayList<Member> memberModels){
        memberList = new ArrayList<>();
        memberList.addAll(memberModels);
        notifyDataSetChanged();
    }

    public final ArrayList<Member> getMemberList() {
        return this.memberList;
    }

    public MemberAdapter(Context c, ArrayList<Member> memList) {
        super();
        this.c = c;
        this.memberList = memList;
    }

    public final class MemberViewHolder extends ViewHolder {

        private TextView name;
        private TextView email;
        private ImageView mMenus;
        //private LinearLayout memTile;
        private final View v;

        public final TextView getName() {
            return this.name;
        }
        public final TextView getEmail() {
            return this.email;
        }

        private void popupMenus(View v) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            Object obj = MemberAdapter.this.getMemberList().get(this.getAdapterPosition());
            final Member position = (Member)obj;
            PopupMenu popupMenus = new PopupMenu(getV().getContext(), v);

            popupMenus.inflate(R.menu.remove_member_menu);
            popupMenus.setOnMenuItemClickListener(it -> {
                //boolean bool;
                /*switch(it.getItemId()) {
                    /*case R.id.editText:
                        View v1 = LayoutInflater.from(getV().getContext()).inflate(R.layout.add_member_item, null);
                        final EditText memName = v1.findViewById(R.id.memberName);
                        final EditText memDetail = v1.findViewById(R.id.memberEmail);
                        (new Builder(getV().getContext())).setView(v1).setPositiveButton("Ok", (dialog, $noName_1) -> {
                            Member pos = position;
                            EditText editText = memName;
                            String names = editText.getText().toString();
                            editText = memDetail;
                            String emails = editText.getText().toString();
                            if(names.equals("") || emails.equals("")){
                                Toast.makeText(getV().getContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                pos.setMemberName(names);
                                pos.setMemberEmail(emails);
                                MemberAdapter.this.notifyDataSetChanged();
                                Toast.makeText(getV().getContext(), "Member Information is Edited", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }).setNegativeButton("Cancel", null).create().show();
                        bool = true;
                        break;*/
                    //case R.id.remove:
                        (new Builder(getV().getContext())).setTitle("Remove Member").setIcon(R.drawable.ic_warning).setMessage("Are you sure to remove this Member").setPositiveButton("Yes", (dialog, $noName_1) -> {
                            MemberAdapter.this.getMemberList().remove(MemberViewHolder.this.getAdapterPosition());
                            MemberAdapter.this.notifyDataSetChanged();
                            Toast.makeText(getV().getContext(), "Removed this Member", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }).setNegativeButton("No", null).create().show();
                        //bool = true;
                        //break;
//                    default:
//                        bool = true;
                //}

                return true;
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

        public MemberViewHolder(View v) {
            super(v);
            this.v = v;
            View view = this.v.findViewById(R.id.memName);
            this.name = (TextView)view;
            view = this.v.findViewById(R.id.memEmail);
            this.email = (TextView)view;
            view = this.v.findViewById(R.id.mMenus);
            this.mMenus = (ImageView)view;
            //view = this.v.findViewById(R.id.memTile);
            //this.memTile = (LinearLayout)view;
            this.mMenus.setOnClickListener(it -> {
                MemberViewHolder viewHolder = MemberViewHolder.this;
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
