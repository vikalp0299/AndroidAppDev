package com.example.navigationbar.AuthenticationActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.navigationbar.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button next;
    TextView forgotPasswword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_email_input);
        password = findViewById(R.id.login_password);
        forgotPasswword = findViewById(R.id.login_forgot_password_button);
        next = findViewById(R.id.login_next);
    }

    @Override
    protected void onResume() {
        super.onResume();

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Helper.validatePassword(s.toString())){

                }
            }
        });

    }
}