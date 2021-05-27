package com.example.navigationbar.AuthenticationActivities;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class WSSEmitters {
    public Emitter.Listener onRegistration = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject json = (JSONObject) args[0];
            try {
                String data = json.getString("status");
                EventBus.getDefault().post(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
