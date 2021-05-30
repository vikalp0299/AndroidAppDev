package com.example.connect.AuthenticationActivities;

import android.util.Log;

public class RoomDeletionEvent{
    public boolean status;
    public long id;
    RoomDeletionEvent(boolean status){
        this.status = status;
    }
    RoomDeletionEvent(boolean status, long id) {
        this.status = status;
        this.id = id;
    }
}