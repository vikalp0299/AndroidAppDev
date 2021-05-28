package com.example.connect.AuthenticationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.MainActivity;
import com.example.connect.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button next;
    TextView forgotPassword;
    boolean isValidEmail;
    WebSocketService wss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_email_input);
        password = findViewById(R.id.login_password);
        forgotPassword = findViewById(R.id.login_forgot_password_button);
        next = findViewById(R.id.login_next);
        wss = (WebSocketService) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginResultEvent(LoginEvent event){
        switch (event.status){
            case 200:{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            }
            case 300:{
                Intent intent = new Intent(getApplicationContext(), UserValidationActivity.class);
                startActivity(intent);
                break;
            }
            case 400:{
                Toast.makeText(getApplicationContext(),"Invalid user credentials",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!Helper.validateEmailAddress(email.getText().toString())){
                        isValidEmail = false;
                        email.setError("Invalid Email Format");
                    }else{
                        isValidEmail = true;
                    }
                }
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Helper.validateEmailAddress(s.toString())){
                    isValidEmail = true;
                    email.setError(null);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail){
                    JSONObject json = new JSONObject();
                    try {
                        json.put("email",email.getText().toString());
                        json.put("password",password.getText().toString());
                        wss.fireDataToServer(WebSocketService.LOGIN,json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"invalid email format",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}