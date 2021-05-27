package com.example.navigationbar.AuthenticationActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.navigationbar.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText email,firstName,lastName,password,confirmPassword;
    String currentlyEditing;
    Button nextButton;
    ProgressBar emailLoader,passwordLoader,confirmPasswordLoader;
    Boolean isValidEmail,isValidFirstName,isValidLastName,isValidPassword,isMatchingPassword;
    WebSocketService wss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        wss = (WebSocketService) getApplication();
        isValidEmail = false;
        isMatchingPassword = false;
        isValidFirstName = false;
        isValidLastName = false;
        isValidPassword = false;
        email = findViewById(R.id.register_email_input);
        firstName = findViewById(R.id.register_first_name);
        lastName = findViewById(R.id.register_last_name);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirm_password);
        nextButton = findViewById(R.id.register_next);
        emailLoader = findViewById(R.id.register_email_loader);
        passwordLoader = findViewById(R.id.register_password_loader);
        confirmPasswordLoader = findViewById(R.id.register_confirm_password_loader);

        emailLoader.setVisibility(View.INVISIBLE);
        passwordLoader.setVisibility(View.INVISIBLE);
        confirmPasswordLoader.setVisibility(View.INVISIBLE);
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

    @Override
    protected void onResume() {
        super.onResume();
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!Helper.validateEmailAddress(email.getText().toString())){
                        isValidEmail = false;
                        emailLoader.setVisibility(View.INVISIBLE);
                        email.setError("Invalid Email Format");
                        email.setPadding(email.getPaddingLeft(),email.getPaddingTop(),10, email.getPaddingBottom());
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
                currentlyEditing = "EMAIL";
                emailLoader.setVisibility(View.VISIBLE);
                email.setPadding(email.getPaddingLeft(),email.getPaddingTop(),25, email.getPaddingBottom());
                if(Helper.validateEmailAddress(s.toString())){
                    isValidEmail = true;
                    emailLoader.setVisibility(View.INVISIBLE);
                    email.setPadding(email.getPaddingLeft(),email.getPaddingTop(),10, email.getPaddingBottom());
                    email.setCompoundDrawablesWithIntrinsicBounds(null,null,getApplicationContext().getDrawable(R.drawable.checkmark),null);
                    // TODO:verify the integrity with server
                }else{
                    email.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                }
            }
        });

        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (firstName.getText().toString().isEmpty()){
                        isValidFirstName = false;
                        firstName.setError("Can't be Empty");
                    }else{
                        isValidFirstName = true;
                    }
                }
            }
        });

        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (lastName.getText().toString().isEmpty()){
                        isValidLastName = false;
                        lastName.setError("Can't be Empty");
                    }else{
                        isValidLastName = true;
                    }
                }
            }
        });


        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("in password");
                if (!hasFocus){
                    if (!Helper.validatePassword(password.getText().toString())){
                        isValidPassword = false;
                        password.setError("Requires a stronger password");
                    }else{
                        isValidPassword = true;
                        if (password.getText().toString().equals(confirmPassword.getText().toString())){
                            confirmPassword.setError(null);
                        }
                    }
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(confirmPassword.getText().toString())){
                    isMatchingPassword = true;
                    confirmPassword.setError(null);
                }else{
                    isMatchingPassword = false;
                    confirmPassword.setError("should match with password");
                }
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!password.getText().toString().equals(s.toString())){
                    isMatchingPassword = false;
                    confirmPassword.setError("should match with password");
                }else{
                    isMatchingPassword = true;
                }
            }
        });

        confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!password.getText().toString().equals(confirmPassword.getText().toString())){
                        isMatchingPassword = false;
                        confirmPassword.setError("should match with password");
                    }else{
                        isMatchingPassword = true;
                    }
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail && isValidFirstName && isValidLastName && isValidPassword && isMatchingPassword){
                    JSONObject json = new JSONObject();
                    String emailString = email.getText().toString().toLowerCase();
                    try {
                        json.put("email",emailString);
                        json.put("firstName",firstName.getText().toString());
                        json.put("lastName",lastName.getText().toString());
                        json.put("password",password.getText().toString());
                        wss.fireDataToServer(WebSocketService.REGISTER_USER,json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println(""+isValidEmail+isValidFirstName+isValidLastName+isValidPassword+isMatchingPassword);
                }
            }
        });
    }

    @Subscribe
    public void onRegistration(String response){
        System.out.println(response);
    }

}