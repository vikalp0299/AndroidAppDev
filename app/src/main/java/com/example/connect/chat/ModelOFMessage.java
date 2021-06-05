package com.example.connect.chat;

import androidx.annotation.Nullable;


import com.example.connect.chat.commons.models.IMessage;
import com.example.connect.chat.commons.models.MessageContentType;

import java.util.Date;

public class ModelOFMessage implements IMessage, MessageContentType.Image {
    String id;
    String text;
    com.example.connect.chat.ModelOFUser user;
    Date createdAt;
    Image image;

    public ModelOFMessage(String id, com.example.connect.chat.ModelOFUser user, String text){
        this(id,user,text,new Date());
    }
    public ModelOFMessage(String id, com.example.connect.chat.ModelOFUser user, String text, Date createdAt){
        this.id = id;
        this.user = user;
        this.text=text;
        this.createdAt= createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public ModelOFUser getUser() {
        return this.user;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Nullable
    @Override
    public String getImageUrl() {
        return image == null ? null : image.url ;
    }

    public String getStatus() {
        return "sent";
    }

    public static class Image{
        protected String url;
        public Image(String url){
            this.url =url;
        }
    }

    public void setImage(Image image){
        this.image = image;
    }

    public void setCreatedAt(Date createdAt){
        this.createdAt=createdAt;
    }
}
