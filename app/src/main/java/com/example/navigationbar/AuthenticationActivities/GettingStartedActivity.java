package com.example.navigationbar.AuthenticationActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.navigationbar.R;

import io.socket.client.Socket;

public class GettingStartedActivity extends AppCompatActivity {

    Button createAccountButton;
    TextView loginButton;
    private Socket client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);


        createAccountButton = findViewById(R.id.createAccountButton);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLogin = new Intent(getApplicationContext(),LoginActivity.class);
                GettingStartedActivity.this.startActivity(goToLogin);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegister = new Intent(getApplicationContext(),RegisterActivity.class);
                GettingStartedActivity.this.startActivity(goToRegister);
            }
        });
    }
}