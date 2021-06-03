package com.example.connect.AuthenticationActivities;

import android.util.Log;

import com.example.connect.AuthenticationActivities.Events.GetNotificationEvent;
import com.example.connect.AuthenticationActivities.Events.GotMembersEvent;
import com.example.connect.AuthenticationActivities.Events.InvitedMemberEvent;
import com.example.connect.AuthenticationActivities.Events.GetRoomsEvent;
import com.example.connect.AuthenticationActivities.Events.LoginEvent;
import com.example.connect.AuthenticationActivities.Events.RegistrationEvent;
import com.example.connect.AuthenticationActivities.Events.RespondToNotificationEvent;
import com.example.connect.AuthenticationActivities.Events.RoomCreationEvent;
import com.example.connect.AuthenticationActivities.Events.RoomDeletionEvent;
import com.example.connect.AuthenticationActivities.Events.RoomEditedEvent;
import com.example.connect.AuthenticationActivities.Events.SearchUsersEvent;
import com.example.connect.AuthenticationActivities.Events.UniqueEmailEvent;
import com.example.connect.AuthenticationActivities.Events.VerificationEvent;
import com.example.connect.Entities.AuthUser;
import com.example.connect.Entities.DaoSession;
import com.example.connect.Entities.InvitationNotification;
import com.example.connect.Entities.Room;
import com.example.connect.Entities.RoomDao;
import com.example.connect.Entities.RoomMember;
import com.example.connect.Entities.RoomMemberDao;
import com.example.connect.model.SearchUser;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class WSSEmitters {

    DaoSession daoSession;

    public WSSEmitters(DaoSession daoSession){
        this.daoSession = daoSession;
    }

    public Emitter.Listener onRegistration = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                boolean status = json.getBoolean("status");
                JSONObject data = json.getJSONObject("data");
                if (status){
                    String uid = data.getString("uid");
                    String email = data.getString("email");
                    String firstName = data.getString("firstName");
                    String lastName = data.getString("lastName");
                    String joined = data.getString("joined");

                    AuthUser user = new AuthUser();
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setUid(uid);
                    user.setVerified(false);
                    user.setTimeStamp(Long.parseLong(joined));
                    daoSession.getAuthUserDao().deleteAll();
                    daoSession.clear();
                    System.out.println("user created");
                    daoSession.getAuthUserDao().insert(user);
                    EventBus.getDefault().post(new RegistrationEvent(true,user));
                    return;
                }
                EventBus.getDefault().post(new RegistrationEvent(false));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public Emitter.Listener isUniqueEmail = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                boolean status = json.getBoolean("status");
                String email = json.getString("email");
                EventBus.getDefault().post(new UniqueEmailEvent(status,email));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public Emitter.Listener onAuthToken = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                boolean status = json.getBoolean("status");
                if (status) {
                    String token = json.getString("token");
                    AuthUser user = daoSession.getAuthUserDao().queryBuilder().list().get(0);
                    user.setVerified(true);
                    user.setToken(token);
                    daoSession.getAuthUserDao().update(user);
                    remoteLoadRooms();
                }
                EventBus.getDefault().post(new VerificationEvent(status));
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new VerificationEvent(false));
        }
    };

    public void remoteLoadRooms(){
        JSONObject json = new JSONObject();
        try {
            WebSocketService wss = WebSocketService.getWebSocketService();
            json.put("uid",wss.getAuthUser().getUid());
            wss.fireDataToServer(WebSocketService.GET_ROOMS,json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                int status = json.getInt("status");
                switch (status){
                    case 200: {
                        JSONObject jsonUser = json.getJSONObject("user");
                        AuthUser user = new AuthUser();
                        user.setUid(jsonUser.getString("uid"));
                        user.setFirstName(jsonUser.getString("firstName"));
                        user.setLastName(jsonUser.getString("lastName"));
                        user.setEmail(jsonUser.getString("email"));
                        user.setToken(jsonUser.getString("token"));
                        user.setPictureUrl("");
                        user.setVerified(true);
                        user.setTimeStamp(jsonUser.getLong("joined"));
                        daoSession.getAuthUserDao().deleteAll();
                        daoSession.getAuthUserDao().insert(user);
                        WebSocketService.getWebSocketService().clearAuthUser();
                        remoteLoadRooms();
                        break;
                    }
                    case 300: {
                        JSONObject jsonUser = json.getJSONObject("user");
                        AuthUser user = new AuthUser();
                        user.setUid(jsonUser.getString("uid"));
                        user.setFirstName(jsonUser.getString("firstName"));
                        user.setLastName(jsonUser.getString("lastName"));
                        user.setEmail(jsonUser.getString("email"));
                        user.setTimeStamp(jsonUser.getLong("joined"));
                        user.setVerified(false);
                        daoSession.getAuthUserDao().deleteAll();
                        daoSession.getAuthUserDao().insert(user);
                        break;
                    }
                    case 400: {
                        break;
                    }
                }
                EventBus.getDefault().post(new LoginEvent(status));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };
    public Emitter.Listener onCreatedRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                boolean status = json.getBoolean("status");

                if(status){
                    JSONObject response = json.getJSONObject("response");
                    String rid = response.getString("rid");
                    String name = response.getString("name");
                    String description = response.getString("description");
                    String createdBy = response.getString("createdBy");
                    Room room = new Room();
                    room.setName(name);
                    room.setCreatedByUser(createdBy);
                    room.setDescription(description);
                    room.setRid(rid);
                    daoSession.getRoomDao().insertWithoutSettingPk(room);
                    EventBus.getDefault().post(new RoomCreationEvent(true,room));
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new RoomCreationEvent(false));
        }
    };
    public Emitter.Listener onDeletedRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("Deleted", "i hope its working");
            JSONObject json = (JSONObject) args[0];
            try {
                boolean status = json.getBoolean("status");
                if(status){
                    String name = json.getString("name");
                    String description = json.getString("description");
                    String rid = json.getString("rid");
                    Room room = new Room();
                    room.setRid(rid);
                    room.setName(name);
                    room.setDescription(description);
                    daoSession.getRoomDao().queryBuilder().where(RoomDao.Properties.Rid.eq(rid)).buildDelete().executeDeleteWithoutDetachingEntities();
                    daoSession.clear();
                    EventBus.getDefault().post(new RoomDeletionEvent(true,room));
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new RoomDeletionEvent(false));
        }
    };

    public Emitter.Listener onEditedRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                boolean status = json.getBoolean("status");
                if(status){
                    JSONObject data = json.getJSONObject("response");
                    String name = data.getString("name");
                    String description = data.getString("description");
                    String rid = data.getString("rid");
                    Room room = daoSession.getRoomDao().queryBuilder().where(RoomDao.Properties.Rid.eq(rid)).unique();
                    room.setName(name);
                    room.setDescription(description);
                    daoSession.getRoomDao().update(room);
                    daoSession.clear();
                    EventBus.getDefault().post(new RoomEditedEvent(true,room));
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new RoomEditedEvent(false));
        }
    };

    public Emitter.Listener onSearchedUsers = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try{
                boolean status = json.getBoolean("status");
                if (status){
                    JSONArray result = json.getJSONArray("result");
                    ArrayList<SearchUser> searchUsers = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject data = result.getJSONObject(i);
                        String uid = data.getString("uid");
                        if (uid.equals(WebSocketService.getWebSocketService().getAuthUser().getUid())){
                            continue;
                        }
                        String email = data.getString("email");
                        String pictureUrl = data.getString("pictureUrl");
                        String name = data.getString("name");
                        boolean isInvited = data.getBoolean("isInvited");
                        SearchUser user = new SearchUser(uid,name,email,pictureUrl,isInvited);
                        searchUsers.add(user);
                    }
                    EventBus.getDefault().post(new SearchUsersEvent(true, searchUsers));
                    return;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            EventBus.getDefault().post(new SearchUsersEvent(false));
        }
    };

    public Emitter.Listener onInvitedMember = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                boolean status = json.getBoolean("status");
                if (status){
                    String sid = json.getString("sid");
                    String rid = json.getString("rid");
                    EventBus.getDefault().post(new InvitedMemberEvent(true,rid,sid));
                }
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new InvitedMemberEvent(false));
        }
    };

    public Emitter.Listener onJoinedRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //TODO : load conversations
        }
    };

    public Emitter.Listener onTakeRooms = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            boolean status = false;
            try {
                status = json.getBoolean("status");
                if (status){
                    JSONArray results = json.getJSONArray("result");
                    ArrayList<Room> rooms = new ArrayList<>();
                    for (int i = 0;i<results.length();i++){
                        JSONObject data = results.getJSONObject(i);
                        String rid = data.getString("rid");
                        String name = data.getString("name");
                        String description = data.getString("description");
                        String createdBy = data.getString("createdBy");

                        Room room = new Room();
                        room.setName(name);
                        room.setCreatedByUser(createdBy);
                        room.setRid(rid);
                        room.setDescription(description);
                        rooms.add(room);
                    }
                    daoSession.getRoomDao().deleteAll();
                    rooms.forEach(room -> {
                        daoSession.getRoomDao().insertWithoutSettingPk(room);
                    });
                    EventBus.getDefault().post(new GetRoomsEvent(true));
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new GetRoomsEvent(false));
        }
    };

    public Emitter.Listener onRoomInvited = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                String rid = json.getString("rid");
                String roomName = json.getString("roomName");
                String senderName = json.getString("senderName");
                String senderImage = json.getString("senderImage");
                String senderId = json.getString("senderId");
                String response = json.getString("response");
                InvitationNotification notification = new InvitationNotification();
                notification.setRid(rid);
                notification.setSenderName(senderName);
                notification.setRoomName(roomName);
                notification.setPictureUrl(senderImage);
                notification.setSenderId(senderId);
                notification.setResponse(response);
                daoSession.getInvitationNotificationDao().insertWithoutSettingPk(notification);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public Emitter.Listener onGotNotifications = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                boolean status = json.getBoolean("status");
                if (status){
                    daoSession.getInvitationNotificationDao().deleteAll();
                    daoSession.clear();
                    JSONArray result = json.getJSONArray("result");
                    for (int i = 0; i< result.length();i++){
                        JSONObject notificationData = result.getJSONObject(i);
                        String rid = notificationData.getString("rid");
                        String roomName = notificationData.getString("roomName");
                        String senderName = notificationData.getString("senderName");
                        String senderImage = notificationData.getString("senderImage");
                        String senderId = notificationData.getString("senderId");
                        String response = notificationData.getString("response");
                        InvitationNotification notification = new InvitationNotification();
                        notification.setRid(rid);
                        notification.setSenderName(senderName);
                        notification.setRoomName(roomName);
                        notification.setPictureUrl(senderImage);
                        notification.setSenderId(senderId);
                        notification.setResponse(response);
                        daoSession.getInvitationNotificationDao().insertWithoutSettingPk(notification);
                    }
                    EventBus.getDefault().post(new GetNotificationEvent(true));
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new GetNotificationEvent(false));
        }
    };

    public Emitter.Listener onAddedMember = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                String mid = json.getString("mid");
                String email = json.getString("email");
                String name = json.getString("name");
                String rid = json.getString("rid");
                String pictureUrl = json.getString("pictureUrl");
                RoomMember member = new RoomMember();
                member.setRid(rid);
                member.setPictureUrl(pictureUrl);
                member.setName(name);
                member.setEmail(email);
                member.setMid(mid);
                daoSession.getRoomMemberDao().insertWithoutSettingPk(member);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    public Emitter.Listener onAcceptedInvitation = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject)args[0];
            try {
                boolean status = json.getBoolean("status");
                if (status){
                    String rid = json.getString("rid");
                    String name = json.getString("name");
                    String description = json.getString("description");
                    String createdBy = json.getString("createdBy");
                    Room room = new Room();
                    room.setName(name);
                    room.setCreatedByUser(createdBy);
                    room.setRid(rid);
                    room.setDescription(description);
                    daoSession.getRoomDao().insertWithoutSettingPk(room);
                    EventBus.getDefault().post(new RespondToNotificationEvent(true));
                }
                EventBus.getDefault().post(new RespondToNotificationEvent(false));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    public Emitter.Listener onRejectedInvitation = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject)args[0];
            try {
                boolean status = json.getBoolean("status");
                EventBus.getDefault().post(new RespondToNotificationEvent(status));
            }catch (JSONException e){
                e.printStackTrace();
            }
            EventBus.getDefault().post(new RespondToNotificationEvent(false));
        }
    };

    public Emitter.Listener onGotMembers = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                String rid = json.getString("rid");
                if (json.getBoolean("status")){
                    JSONArray results = json.getJSONArray("result");
                    ArrayList<RoomMember> members = new ArrayList<>();
                    for(int i=0;i<results.length();i++){
                        JSONObject jsonMember = results.getJSONObject(i);
                        String mid = jsonMember.getString("mid");
                        String name = jsonMember.getString("name");
                        String email = jsonMember.getString("email");
                        String pictureUrl = jsonMember.getString("pictureUrl");
                        RoomMember member = new RoomMember();
                        member.setMid(mid);
                        member.setEmail(email);
                        member.setName(name);
                        member.setRid(rid);
                        member.setPictureUrl(pictureUrl);
                        members.add(member);
                    }
                    daoSession.getRoomMemberDao().queryBuilder().where(RoomMemberDao.Properties.Rid.eq(rid)).buildDelete().executeDeleteWithoutDetachingEntities();
                    members.forEach(member->{
                        daoSession.getRoomMemberDao().insertWithoutSettingPk(member);
                    });
                    EventBus.getDefault().post(new GotMembersEvent(true));
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new GotMembersEvent(false));
        }
    };
}



