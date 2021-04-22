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


    private EditText Password, LearnerId;
    private TextView ForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // declaring object of EditText control
        LearnerId = (EditText) findViewById(R.id.LearnerId);
        Password = (EditText) findViewById(R.id.Password);
        ForgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        Button LoginBtn = (Button) findViewById(R.id.LoginBtn);

        ForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ForgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(ForgotPasswordIntent);
            }
        });

        LoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String LId = LearnerId.getText().toString();
                String Pwd = Password.getText().toString();

                if(LId.equalsIgnoreCase("ADMIN") && Pwd.equals("ADMIN")){
                    Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    Toast.makeText(LoginActivity.this,"You are Signed in Successfully", Toast.LENGTH_LONG).show();
                    startActivity(MainIntent);
                    finish();
                }else
                {
                    if(LId.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"enter learner id",Toast.LENGTH_SHORT).show();
                    }
                    else if(Pwd.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"enter password",Toast.LENGTH_SHORT).show();
                    }
                    else{
                    Toast.makeText(LoginActivity.this, "User Name or Password is incorrect", Toast.LENGTH_LONG).show();
                }
                }
            }
        });

    }
}

