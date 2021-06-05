package com.example.connect.AuthenticationActivities.Events;

import com.example.connect.chat.ModelOFDialog;

import java.util.ArrayList;

public class GetDialogsEvent {
    public boolean status;
    public ArrayList<ModelOFDialog> dialogs;

    public GetDialogsEvent(boolean status) {
        this.status = status;
    }

    public GetDialogsEvent(boolean status, ArrayList<ModelOFDialog> dialogs) {
        this.status = status;
        this.dialogs = dialogs;
    }
}
