package com.example.connect.AuthenticationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.MainActivity;
import com.example.connect.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class UserValidationActivity extends AppCompatActivity {
    WebSocketService wss;
    Button continueToSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_validation);
        wss = (WebSocketService) getApplication();
        continueToSignInButton = findViewById(R.id.user_validation_continue_to_sign_in);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        verifyEmail();
    }

    public void verifyEmail(){
        JSONObject json = new JSONObject();
        try {
            json.put("uid",wss.getAuthUser().getUid());
            wss.fireDataToServer(WebSocketService.IS_VERIFIED,json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        continueToSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEmail();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasVerifiedEmail(VerificationEvent event){
        if (event.status){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            UserValidationActivity.this.startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"Please verify from your email first",Toast.LENGTH_SHORT).show();
        }
    }
}