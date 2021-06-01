package com.example.connect.AuthenticationActivities.Events;

public class AddedMemberEvent {
    public boolean status;
    public String rid,sid;
    public AddedMemberEvent(boolean status){
        this.status = status;
    }

    public AddedMemberEvent(boolean status, String rid, String sid) {
        this.status = status;
        this.rid = rid;
        this.sid = sid;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
