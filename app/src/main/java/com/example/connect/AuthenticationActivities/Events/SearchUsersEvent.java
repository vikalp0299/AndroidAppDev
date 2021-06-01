package com.example.connect.AuthenticationActivities.Events;

import com.example.connect.model.SearchUser;

import java.util.ArrayList;
import java.util.List;

public class SearchUsersEvent {
    public boolean status;
    public ArrayList<SearchUser> searchUsers;
    public SearchUsersEvent(boolean status){
        this.status = status;
    }
    public SearchUsersEvent(boolean status, ArrayList<SearchUser> searchUsers){
        this.status = status;
        this.searchUsers = searchUsers;
    }
}
