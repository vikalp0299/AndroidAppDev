package com.example.connect.fragments.BottomNavigationFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.connect.AuthenticationActivities.Events.GetNotificationEvent;
import com.example.connect.AuthenticationActivities.Events.RespondToNotificationEvent;
import com.example.connect.AuthenticationActivities.WebSocketService;
import com.example.connect.Entities.InvitationNotification;
import com.example.connect.R;
import com.example.connect.adapters.RoomSectionAdapters.NotificationAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<InvitationNotification> notifications = new ArrayList<>();
    WebSocketService webSocketService = WebSocketService.getWebSocketService();
    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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
    public void onGetNotifications(GetNotificationEvent event){
        if (event.status){
            loadFromDao();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseToNotification(GetNotificationEvent event){
        if (event.status){
            loadRemoteNotifications();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseToAccept(RespondToNotificationEvent event){
        if (event.status){
            loadFromDao();
            adapter.notifyDataSetChanged();
        }
    }


    public void loadFromDao(){
        notifications.clear();
        notifications.addAll(webSocketService.getDaoSession().getInvitationNotificationDao().loadAll());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadFromDao();
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        swipeRefreshLayout = v.findViewById(R.id.notification_swipe_refresh);
        recyclerView = v.findViewById(R.id.notification_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRemoteNotifications();
            }
        });
        return v;
    }

    public void loadRemoteNotifications(){
        JSONObject json = new JSONObject();
        try {
            json.put("uid",webSocketService.getAuthUser().getUid());
            webSocketService.fireDataToServer(WebSocketService.GET_NOTIFICATIONS,json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}