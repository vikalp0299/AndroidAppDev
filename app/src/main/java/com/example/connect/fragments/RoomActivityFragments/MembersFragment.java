package com.example.connect.fragments.RoomActivityFragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.connect.AuthenticationActivities.Events.GotMembersEvent;
import com.example.connect.AuthenticationActivities.Events.InvitedMemberEvent;
import com.example.connect.AuthenticationActivities.Events.SearchUsersEvent;
import com.example.connect.AuthenticationActivities.WebSocketService;
import com.example.connect.Entities.RoomMember;
import com.example.connect.Entities.RoomMemberDao;
import com.example.connect.R;
import com.example.connect.adapters.RoomSectionAdapters.MemberAdapter.MemberAdapter;

import com.example.connect.adapters.RoomSectionAdapters.SearchUserAdapter;
import com.example.connect.model.SearchUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MembersFragment extends Fragment implements SearchView.OnQueryTextListener{
    public ArrayList<RoomMember> members = new ArrayList<RoomMember>();
    public MemberAdapter adapter;
    private Dialog dialog;
    private SwipeRefreshLayout memberRecyclerRefresher;
    WebSocketService wss = WebSocketService.getWebSocketService();
    RecyclerView searchRecyclerView;
    private SearchUserAdapter searchUserAdapter;
    private ArrayList<SearchUser> searchUsers = new ArrayList<>();


    public MembersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchTextResultsReceived(SearchUsersEvent event){
        if (event.status){
            searchUserAdapter.updateList(event.searchUsers);
        }else{
            searchUserAdapter.updateList(new ArrayList<SearchUser>());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMemberAdded(InvitedMemberEvent event){
        if (event.status){
            searchUserAdapter.updateListItem(event.sid);
        }else {
            Toast.makeText(getContext(),"Failed to add member",Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGotRemoteMembers(GotMembersEvent event){
        if (event.status){
            loadDataFromDao();
            adapter.setMemberList(members);
            memberRecyclerRefresher.setRefreshing(false);
        }
    }

    public void loadDataFromDao(){
        members.clear();
        members.addAll(wss.getDaoSession().getRoomMemberDao().queryBuilder().where(RoomMemberDao.Properties.Rid.eq(wss.getRoom().getRid())).list());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_members, container, false);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.memberAddingBtn);
        RecyclerView recyclerView = view.findViewById(R.id.memberRecycler);
        memberRecyclerRefresher = view.findViewById(R.id.member_fragment_refresher);
        loadDataFromDao();
        memberRecyclerRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("rid",wss.getRoom().getRid());
                    wss.fireDataToServer(WebSocketService.GET_MEMBERS,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        adapter = new MemberAdapter(getContext(), members);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        floatingActionButton.setVisibility(wss.getAuthUser().getUid().equals(wss.getRoom().getCreatedByUser()) ? View.VISIBLE : View.GONE);
        floatingActionButton.setOnClickListener(it -> MembersFragment.this.addMemberInfo());

        dialog = new Dialog(getContext());
        View dialogView = inflater.inflate(R.layout.user_search_dialog,container,false);
        EditText searchText = dialogView.findViewById(R.id.user_search_field);
        searchRecyclerView = dialogView.findViewById(R.id.search_user_recycler);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchUserAdapter = new SearchUserAdapter(searchUsers,this);
        ViewGroup.LayoutParams params = searchRecyclerView.getLayoutParams();
        params.height = (getActivity().getResources().getDisplayMetrics().heightPixels*3)/5;
        searchRecyclerView.setLayoutParams(params);
        searchRecyclerView.setAdapter(searchUserAdapter);
        Button button = dialogView.findViewById(R.id.search_user_done);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    searchUsers.clear();
                    searchUserAdapter.notifyDataSetChanged();
                    return;
                }
                Log.d("Search text",searchText.getText().toString());
                JSONObject json = new JSONObject();
                try {
                    json.put("searchText",s.toString());
                    json.put("rid",wss.getRoom().getRid());
                    wss.fireDataToServer(WebSocketService.SEARCH_USERS,json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.BottomSheetStyle);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return view;
    }

    //search filter code
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                adapter.setFilter(members);
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
        final ArrayList<RoomMember> filteredModelList = filter(members, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<RoomMember> filter(ArrayList<RoomMember> models, String query) {
        query = query.toLowerCase();

        final ArrayList<RoomMember> filteredModelList = new ArrayList<>();
        for (RoomMember model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void addMemberInfo() {
        dialog.show();
    }

    public static ArrayList<RoomMember> access$getMemberList$p(MembersFragment $this) {
        return $this.members;
    }

    public static MemberAdapter access$getMemberAdapter$p(MembersFragment $this) {
        return $this.adapter;
    }
}
