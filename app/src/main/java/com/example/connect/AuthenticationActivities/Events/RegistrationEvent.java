package com.example.connect.AuthenticationActivities.Events;

import com.example.connect.Entities.AuthUser;

public class RegistrationEvent{
    public boolean status;
    public AuthUser user;
    public RegistrationEvent(Boolean status){
        this.status = status;
    }
    public RegistrationEvent(Boolean status,AuthUser user){
        this.status = status;
        this.user = user;
    }
}
