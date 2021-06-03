package com.example.connect.AuthenticationActivities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.connect.AuthenticationActivities.Events.OpenRoomEvent;
import com.example.connect.Entities.AuthUser;
import com.example.connect.Entities.DaoMaster;
import com.example.connect.Entities.DaoSession;
import com.example.connect.Entities.InvitationNotification;
import com.example.connect.Entities.InvitationNotificationDao;
import com.example.connect.Entities.Room;
import com.example.connect.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.database.Database;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;

public class WebSocketService extends Application {
    public static final String REGISTER_USER = "register_user";
    public static final String IS_VERIFIED = "is_verified";
    public static final String CHECK_UNIQUE_EMAIL = "check_email_unique";
    public static final String IS_UNIQUE_EMAIL = "is_unique_email";
    public static final String ON_REGISTRATION = "on_registration";
    public static final String AUTH_TOKEN="auth_token";
    public static final String LOGIN="login";
    public static final String LOGIN_RESULT = "login_result";
    public static final String CREATE_ROOM = "create_room";
    public static final String CREATED_ROOM = "created_room";
    public static final String DELETED_ROOM = "deleted_room";
    public static final String DELETE_ROOM = "delete_room";
    public static final String EDITED_ROOM = "edited_room";
    public static final String EDIT_ROOM = "edit_room";
    public static final String SEARCH_USERS = "search_users";
    public static final String SEARCHED_USERS = "searched_users";
    public static final String ADD_MEMBER = "add_member";
    public static final String INVITED_MEMBER = "invited_member";
    public static final String ROOM_INVITED = "room_invited";
    public static final String GET_NOTIFICATIONS = "get_notifications";
    public static final String GOT_NOTIFICATIONS = "got_notifications";
    public static final String ACCEPT_INVITATION="accept_invitation";
    public static final String ACCEPTED_INVITATION="accepted_invitation";
    public static final String REJECT_INVITATION="reject_invitation";
    public static final String REJECTED_INVITATION="rejected_invitation";
    public static final String GET_MEMBERS = "get_members";
    public static final String GOT_MEMBERS = "got_members";
    public static final String REMOVE_MEMBER = "remove_member";
    public static final String REMOVED_MEMBER = "remove_member";
    public static final String JOINED_ROOM = "joined_room";
    public static final String JOIN_ROOM = "join_room";
    public static final String LEAVE_ROOM = "leave_room";
    public static final String LEFT_ROOM = "left_room";
    public static final String GET_ROOMS = "get_rooms";
    public static final String TAKE_ROOMS = "take_rooms";


    private static final String hostUrl = "https://963e0888cbf1.ngrok.io";
    private Socket socket;
    private static Activity currentActivity;
    private DaoSession daoSession;
    private AuthUser authUser;
    private Room room;

    public AuthUser getAuthUser() {
        if (authUser==null){
            if (hasAuthUser()){
               authUser = daoSession.getAuthUserDao().queryBuilder().list().get(0);
            }
        }
        return authUser;
    }

    public void clearAuthUser(){
        this.authUser = null;
    }

    public static WebSocketService webSocketService;

    public static WebSocketService getWebSocketService() {
        return webSocketService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        DataBaseService dataBaseService = new DataBaseService(this,"mad_app");
        Database db = dataBaseService.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        webSocketService = this;
        try {
            IO.Options options = new IO.Options();
            options.query = "uid=" + ((hasAuthUser() && isAuthUserVerified()) ? getAuthUser().getUid() : "");
            WSSEmitters emitters = new WSSEmitters(daoSession);
            socket =IO.socket(hostUrl,options);
            socket.on(ON_REGISTRATION,emitters.onRegistration);
            socket.on(IS_UNIQUE_EMAIL,emitters.isUniqueEmail);
            socket.on(AUTH_TOKEN,emitters.onAuthToken);
            socket.on(LOGIN_RESULT,emitters.onLogin);
            socket.on(JOINED_ROOM,emitters.onJoinedRoom);
            socket.on(CREATED_ROOM,emitters.onCreatedRoom);
            socket.on(DELETED_ROOM,emitters.onDeletedRoom);
            socket.on(EDITED_ROOM,emitters.onEditedRoom);
            socket.on(SEARCHED_USERS,emitters.onSearchedUsers);
            socket.on(INVITED_MEMBER,emitters.onInvitedMember);
            socket.on(TAKE_ROOMS,emitters.onTakeRooms);
            socket.on(ROOM_INVITED,emitters.onRoomInvited);
            socket.on(GOT_NOTIFICATIONS,emitters.onGotNotifications);
            socket.on(ACCEPTED_INVITATION,emitters.onAcceptedInvitation);
            socket.on(REJECTED_INVITATION,emitters.onRejectedInvitation);
            socket.on(GOT_MEMBERS,emitters.onGotMembers);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                if (activity instanceof GettingStartedActivity){
                    if (hasAuthUser()){
                        if (isAuthUserVerified()){
                            Intent goToHomeActivity = new Intent(getApplicationContext(), MainActivity.class);
                            goToHomeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(goToHomeActivity);
                        }else{
                            Intent goToUserValidationActivity = new Intent(getApplicationContext(), UserValidationActivity.class);
                            activity.startActivity(goToUserValidationActivity);
                        }
                    }
                }
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                currentActivity = null;
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }


    public boolean hasAuthUser() {
       boolean bool =  daoSession.getAuthUserDao().queryBuilder().list().isEmpty();
       return !bool;
    }



    public boolean isAuthUserVerified(){
       List<AuthUser> list = daoSession.getAuthUserDao().queryBuilder().list();
       if (!list.isEmpty()){
           return list.get(0).getVerified();
       }
       return false;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        socket.off();
        socket.disconnect();
        EventBus.getDefault().unregister(this);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public Socket getSocket() {
        return socket;
    }

    public void fireDataToServer(String event, JSONObject json){
        socket.emit(event,json);
    }


    private class DataBaseService extends DaoMaster.OpenHelper {

        public DataBaseService(Context context, String name) {
            super(context, name);
        }

        @Override
        public void onCreate(Database db) {
            super.onCreate(db);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenRoomEvent(OpenRoomEvent event){
        Log.d("room",event.room.getName());
        this.room = event.room;
    }

    public void setRoom(Room room){
        this.room = room;
    }

    public Room getRoom(){
        return this.room;
    }
}


