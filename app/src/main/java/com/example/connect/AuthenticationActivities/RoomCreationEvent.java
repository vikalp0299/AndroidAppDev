package com.example.connect.AuthenticationActivities;

import com.example.connect.Entities.Room;

public class RoomCreationEvent{
    public boolean status;
    public Room room;
    RoomCreationEvent(boolean status){
        this.status = status;
    }
    RoomCreationEvent(boolean status,Room room){
        this.status = status;
        this.room = room;
    }
}