package com.example.navigationbar.AuthenticationActivities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class WebSocketService extends Application {
    public static final String REGISTER_USER = "register_user";
    private static final String hostUrl = "https://56b6b7c6deba.ngrok.io";
    private Socket socket;
    private static Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            WSSEmitters emitters = new WSSEmitters();
            socket =IO.socket(hostUrl);
            socket.on("on_registration",emitters.onRegistration);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

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
}


