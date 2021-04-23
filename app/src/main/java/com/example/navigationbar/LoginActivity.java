package com.example.navigationbar;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {


    private EditText Password, UserName;
    private TextView ForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // declaring object of EditText control
        UserName = (EditText) findViewById(R.id.UserName);
        Password = (EditText) findViewById(R.id.Password);
        ForgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        Button LoginBtn = (Button) findViewById(R.id.LoginBtn);
        Button SignUpBtn = (Button) findViewById(R.id.SignUpBtn);

        ForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ForgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(ForgotPasswordIntent);
            }
        });

        //On pressing Login button
        LoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String Usr = UserName.getText().toString();
                String Pwd = Password.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                //checks if username is empty
                if(Usr.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"enter username",Toast.LENGTH_SHORT).show();
                }

                //checks if password is empty
                else if(Pwd.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"enter password",Toast.LENGTH_SHORT).show();
                }

                //checks if username is valid or not
                else if (!Usr.trim().matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(),"invalid email address",Toast.LENGTH_SHORT).show();
                }

                else {
                    //authentication
                    if (Usr.equalsIgnoreCase("ADMIN@gmail.com") && Pwd.equals("ADMIN")) {
                        Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        Toast.makeText(LoginActivity.this, "You are Signed in Successfully", Toast.LENGTH_LONG).show();
                        startActivity(MainIntent);
                        finish();
                    }

                    //if authentication failed
                    else {
                        Toast.makeText(LoginActivity.this, "User Name or Password is incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //On pressing Sign Up button
        SignUpBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Redirecting...", Toast.LENGTH_LONG).show();
                //TODO: Redirect to web sign up page
            }
        });

    }
}

