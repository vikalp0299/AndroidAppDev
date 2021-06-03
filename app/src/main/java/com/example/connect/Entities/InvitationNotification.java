package com.example.connect.Entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true)
public class InvitationNotification {
    @Id(autoincrement = true)
    public Long id;
    public String senderName;
    public String roomName;
    public String pictureUrl;
    public String response;
    public String senderId;
    @Index
    public String rid;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1817564534)
    private transient InvitationNotificationDao myDao;
    @Generated(hash = 729356297)
    public InvitationNotification(Long id, String senderName, String roomName,
            String pictureUrl, String response, String senderId, String rid) {
        this.id = id;
        this.senderName = senderName;
        this.roomName = roomName;
        this.pictureUrl = pictureUrl;
        this.response = response;
        this.senderId = senderId;
        this.rid = rid;
    }
    @Generated(hash = 1078106744)
    public InvitationNotification() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSenderName() {
        return this.senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getRid() {
        return this.rid;
    }
    public void setRid(String rid) {
        this.rid = rid;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    public String getRoomName() {
        return this.roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public String getPictureUrl() {
        return this.pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public String getResponse() {
        return this.response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    public String getSenderId() {
        return this.senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2098253310)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInvitationNotificationDao() : null;
    }
}
