package com.example.connect.AuthenticationActivities;

import com.example.connect.Entities.Room;

public class RoomEditedEvent {
    public boolean status;
    public Room room;
    RoomEditedEvent(boolean status){
        this.status = status;
    }
    RoomEditedEvent(boolean status,Room room){
        this.status = status;
        this.room = room;
    }
}
