package com.example.connect.AuthenticationActivities.Events;

import android.util.Log;

import com.example.connect.Entities.Room;

public class RoomDeletionEvent{
    public boolean status;
    public Room room;
    public RoomDeletionEvent(boolean status){
        this.status = status;
    }
    public RoomDeletionEvent(boolean status, Room room) {
        this.status = status;
        this.room = room;
    }
}