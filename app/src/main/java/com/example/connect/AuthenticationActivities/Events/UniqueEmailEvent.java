package com.example.connect.AuthenticationActivities.Events;

public class UniqueEmailEvent{
    public boolean status;
    public String email;
    public UniqueEmailEvent(boolean status,String email){
        this.status = status;
        this.email = email;
    }
}
