package com.example.connect.fragments.BottomNavigationFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.example.connect.AuthenticationActivities.Events.GetRoomsEvent;
import com.example.connect.AuthenticationActivities.Events.RoomCreationEvent;
import com.example.connect.AuthenticationActivities.Events.RoomDeletionEvent;
import com.example.connect.AuthenticationActivities.Events.RoomEditedEvent;
import com.example.connect.AuthenticationActivities.WebSocketService;
import com.example.connect.Entities.DaoSession;
import com.example.connect.Entities.Room;
import com.example.connect.R;

import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.connect.adapters.RoomSectionAdapters.RoomAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;


public class RoomFragment extends Fragment implements SearchView.OnQueryTextListener{

    private ArrayList<com.example.connect.Entities.Room> rooms = new ArrayList<>();
    private RoomAdapter adapter;
    WebSocketService webSocketService;
    SwipeRefreshLayout refreshLayout;

    public RoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        webSocketService = (WebSocketService)getActivity().getApplication();
        loadDaoData();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.addingBtn);
        RecyclerView recyclerView = view.findViewById(R.id.mRecycler);
        refreshLayout = view.findViewById(R.id.room_fragment_swipe_refresher);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JSONObject json = new JSONObject();
                try {
                    json.put("uid",webSocketService.getAuthUser().getUid());
                    webSocketService.fireDataToServer(WebSocketService.GET_ROOMS,json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        this.adapter = new RoomAdapter(getContext(), rooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        floatingActionButton.setOnClickListener(it -> RoomFragment.this.addInfo());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    //search filter code
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                adapter.setFilter(rooms);
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true; // Return true to expand action view
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<Room> filteredModelList = filter(rooms, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<Room> filter(ArrayList<Room> models, String query) {
        query = query.toLowerCase();

        final ArrayList<Room> filteredModelList = new ArrayList<>();
        for (Room model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    //Event listeners
    @Override
    public void onStart() {
        super.onStart();
        webSocketService.setRoom(null);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDelete(RoomDeletionEvent event){
        if (event.status){
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            if (this.adapter.deletedRoomsHashMap.containsKey(event.room.getRid())){
                int position = this.adapter.deletedRoomsHashMap.get(event.room.getRid());
                RoomFragment.access$getRoomAdapter$p(RoomFragment.this).removeItemFromList(position);
                RoomFragment.access$getRoomAdapter$p(RoomFragment.this).notifyItemRemoved(position);
                RoomFragment.access$getRoomAdapter$p(RoomFragment.this).notifyItemRangeChanged(position,this.adapter.getItemCount()-position);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGotRoomsComplete(GetRoomsEvent event){
        if (event.status){
            loadDaoData();
            this.adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEditComplete(RoomEditedEvent event){
        if (event.status){
            if ( RoomFragment.access$getRoomAdapter$p(RoomFragment.this).editedRoomsHashMap.containsKey(event.room.getRid())){
                int position = this.adapter.editedRoomsHashMap.get(event.room.getRid());
                RoomFragment.access$getRoomAdapter$p(RoomFragment.this).replaceItemFromList(position,event.room);
                RoomFragment.access$getRoomAdapter$p(RoomFragment.this).notifyItemChanged(position);
                Toast.makeText(getContext(), "Room Information is Edited", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRoomAdded(RoomCreationEvent event){
        if(event.status){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RoomFragment.access$getRoomList$p(RoomFragment.this).add(event.room);
                    RoomFragment.access$getRoomAdapter$p(RoomFragment.this).notifyDataSetChanged();
                    Toast.makeText(getContext(), "Added Room Successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //dialog box for adding team
    private void addInfo() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.add_room_item, null);
        final EditText roomName = v.findViewById(R.id.roomName);
        final EditText roomDesc = v.findViewById(R.id.roomDetails);
        Builder addDialog = new Builder(Objects.requireNonNull(getContext()));
        addDialog.setView(v);
        addDialog.setPositiveButton("Ok", (dialog, $noName_1) -> {
            EditText editText = roomName;
            String names = editText.getText().toString();
            editText = roomDesc;
            String details = editText.getText().toString();
            if(names.equals("") || details.equals("")){
                Toast.makeText(getContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
            }
            else {
                JSONObject json = new JSONObject();
                try {
                    json.put("uid",webSocketService.getAuthUser().getUid());
                    json.put("rid",null);
                    json.put("name",names);
                    json.put("description",details);
                    webSocketService.fireDataToServer(WebSocketService.CREATE_ROOM,json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            dialog.dismiss();
        });
        addDialog.setNegativeButton("Cancel", (dialog, $noName_1) -> {
            dialog.dismiss();
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        });
        addDialog.create();
        addDialog.show();
    }

    public static ArrayList<Room> access$getRoomList$p(RoomFragment $this) {
        return $this.rooms;
    }

    public static RoomAdapter access$getRoomAdapter$p(RoomFragment $this) {
        return $this.adapter;
    }


    //loading data using daoSession
    private void loadDaoData(){
        rooms.clear();
        List<com.example.connect.Entities.Room> roomList = webSocketService.getDaoSession().getRoomDao().queryBuilder().listLazyUncached();
        System.out.println(roomList.size());
        for(Room room : roomList){
            rooms.add(room);
        }

    }

    class RoomNameSorter implements Comparator<Room> {

        @Override
        public int compare(Room o1, Room o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }
}