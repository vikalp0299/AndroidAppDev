package com.example.connect.AuthenticationActivities.Events;

import com.example.connect.Entities.Room;

public class OpenRoomEvent {
    public Room room;
    public OpenRoomEvent(Room room){
        this.room = room;
    }
}
