package com.example.connect.AuthenticationActivities.Events;

import android.util.Log;

import com.example.connect.Entities.Room;

public class RoomEditedEvent {
    public boolean status;
    public Room room;
    public RoomEditedEvent(boolean status){
        this.status = status;
    }
    public RoomEditedEvent(boolean status,Room room){
        Log.d("room edit event", room.getName());
        this.status = status;
        this.room = room;
    }
}
