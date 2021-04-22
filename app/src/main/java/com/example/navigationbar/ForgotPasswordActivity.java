package com.example.navigationbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText Email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Email = (EditText) findViewById(R.id.Email);
        Button EnterEmailBtn = (Button) findViewById(R.id.EnterEmailBtn);

        EnterEmailBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String EmailId = Email.getText().toString();
                if(EmailId.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"enter email address",Toast.LENGTH_SHORT).show();
                }
                else if (!EmailId.trim().matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(),"invalid email address",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent LoginIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    Toast.makeText(ForgotPasswordActivity.this, "Password has been sent to " + EmailId, Toast.LENGTH_LONG).show();
                    startActivity(LoginIntent);
                    finish();
                }
            }
        });
    }
}