package com.example.connect.AuthenticationActivities;

import android.util.Log;

import com.example.connect.Entities.AuthUser;
import com.example.connect.Entities.DaoSession;
import com.example.connect.Entities.Room;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.database.Database;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

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
                    System.out.println("user created");
                    daoSession.getAuthUserDao().insert(user);
                    EventBus.getDefault().post(new RegistrationEvent(status,user));
                    return;
                }
                EventBus.getDefault().post(new RegistrationEvent(status));
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
                }
                EventBus.getDefault().post(new VerificationEvent(status));
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new VerificationEvent(false));
        }
    };

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
                    room.setId(Helper.getUniqueLongID());
                    daoSession.getRoomDao().insert(room);
                    EventBus.getDefault().post(new RoomCreationEvent(status,room));

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
                    long id = json.getLong("id");
                    String name = json.getString("name");
                    String description = json.getString("description");
                    String rid = json.getString("rid");
                    Room room = new Room();
                    room.setRid(rid);
                    room.setName(name);
                    room.setDescription(description);
                    room.setId(id);
                    daoSession.getRoomDao().deleteByKey(id);
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
                    Log.d("name :::::: ", name);
                    String description = data.getString("description");
                    long id = data.getLong("id");
                    String rid = data.getString("rid");
                    String createdBy = data.getString("createdBy");
                    Room room = new Room();
                    room.setId(id);
                    room.setRid(rid);
                    room.setName(name);
                    room.setCreatedByUser(createdBy);
                    room.setDescription(description);
                    daoSession.getRoomDao().update(room);
                    EventBus.getDefault().post(new RoomEditedEvent(true,room));
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new RoomEditedEvent(false));
        }
    };




}


class LoginEvent{
    int status;
    LoginEvent(int status){
        this.status = status;
    }
}

class UniqueEmailEvent{
    boolean status;
    String email;
    UniqueEmailEvent(boolean status,String email){
        this.status = status;
        this.email = email;
    }
}



class RegistrationEvent{
    boolean status;
    AuthUser user;
    RegistrationEvent(Boolean status){
        this.status = status;
    }
    RegistrationEvent(Boolean status,AuthUser user){
        this.status = status;
        this.user = user;
    }
}

class VerificationEvent{
    boolean status;
    VerificationEvent(boolean status){
        this.status = status;
    }
}