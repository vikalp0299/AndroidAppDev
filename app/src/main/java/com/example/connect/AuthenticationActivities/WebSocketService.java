package com.example.connect.AuthenticationActivities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.connect.Entities.AuthUser;
import com.example.connect.Entities.DaoMaster;
import com.example.connect.Entities.DaoSession;
import com.example.connect.MainActivity;

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

    private static final String hostUrl = "https://56b6b7c6deba.ngrok.io";
    private Socket socket;
    private static Activity currentActivity;
    private DaoSession daoSession;
    private AuthUser authUser;

    public AuthUser getAuthUser() {
        if (authUser==null){
            if (hasAuthUser()){
               authUser = daoSession.getAuthUserDao().queryBuilder().list().get(0);
            }
        }
        return authUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataBaseService dataBaseService = new DataBaseService(this,"app_db");
        Database db = dataBaseService.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        try {
            WSSEmitters emitters = new WSSEmitters(daoSession);
            socket =IO.socket(hostUrl);
            socket.on(ON_REGISTRATION,emitters.onRegistration);
            socket.on(IS_UNIQUE_EMAIL,emitters.isUniqueEmail);
            socket.on(AUTH_TOKEN,emitters.onAuthToken);
            socket.on(LOGIN_RESULT,emitters.onLogin);
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
//                fireDataToServer(IS_VERIFIED,);
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
}

