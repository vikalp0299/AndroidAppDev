package com.example.connect.model;

import java.util.Objects;

public class SearchUser {
    String sid;
    String name;
    String email;
    String pictureUrl;
    boolean isAlreadyMember;
    boolean isInvited;

    public SearchUser(String sid, String name, String email, String pictureUrl, boolean isInvited) {
        this.sid = sid;
        this.name = name;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.isInvited = isInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }

    public boolean isInvited() {
        return isInvited;
    }

    public SearchUser(String sid, String name, String email, String pictureUrl) {
        this.sid = sid;
        this.name = name;
        this.email = email;
        this.pictureUrl = pictureUrl;
    }

    public SearchUser(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o instanceof String)){ return getSid().equals(o.toString()); }
        else if (!(o instanceof SearchUser)) return false;
        SearchUser that = (SearchUser) o;
        return getSid().equals(that.getSid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSid());
    }

    public void setIsAlreadyMember(boolean isAlreadyMember){
        this.isAlreadyMember = isAlreadyMember;
    }

    public boolean getIsAlreadyMember(){
        return isAlreadyMember;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
