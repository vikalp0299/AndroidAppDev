package com.example.connect.AuthenticationActivities.Events;

import com.example.connect.Entities.Room;

import java.util.ArrayList;

public class GetRoomsEvent {
    public boolean status;

    public GetRoomsEvent(boolean status) {
        this.status = status;
    }
}
