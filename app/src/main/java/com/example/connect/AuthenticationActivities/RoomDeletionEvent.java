package com.example.connect.AuthenticationActivities;

import android.util.Log;

import com.example.connect.Entities.Room;

public class RoomDeletionEvent{
    public boolean status;
    public Room room;
    RoomDeletionEvent(boolean status){
        this.status = status;
    }
    RoomDeletionEvent(boolean status, Room room) {
        this.status = status;
        this.room = room;
    }
}