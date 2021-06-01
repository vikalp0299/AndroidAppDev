package com.example.connect.AuthenticationActivities.Events;

import com.example.connect.Entities.Room;

public class RoomCreationEvent{
    public boolean status;
    public Room room;
    public RoomCreationEvent(boolean status){
        this.status = status;
    }
    public RoomCreationEvent(boolean status,Room room){
        this.status = status;
        this.room = room;
    }
}