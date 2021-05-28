package com.example.navigationbar.Entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;



@Entity(
        active = true
)
public class AuthUser {
    @Id
    public long id;
    @Index(unique = true)
    public String uid;
    @NotNull
    public String firstName;
    @NotNull
    public String lastName;
    @Index(unique = true)
    public String email;
    public String pictureUrl;
    @NotNull
    public Boolean verified;
    private String token;
    @NotNull
    public long timeStamp;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 621821637)
private transient AuthUserDao myDao;
@Generated(hash = 171331386)
public AuthUser(long id, String uid, @NotNull String firstName,
        @NotNull String lastName, String email, String pictureUrl,
        @NotNull Boolean verified, String token, long timeStamp) {
    this.id = id;
    this.uid = uid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.pictureUrl = pictureUrl;
    this.verified = verified;
    this.token = token;
    this.timeStamp = timeStamp;
}
@Generated(hash = 1740224645)
public AuthUser() {
}
public long getId() {
    return this.id;
}
public void setId(long id) {
    this.id = id;
}
public String getUid() {
    return this.uid;
}
public void setUid(String uid) {
    this.uid = uid;
}
public String getFirstName() {
    return this.firstName;
}
public void setFirstName(String firstName) {
    this.firstName = firstName;
}
public String getLastName() {
    return this.lastName;
}
public void setLastName(String lastName) {
    this.lastName = lastName;
}
public String getEmail() {
    return this.email;
}
public void setEmail(String email) {
    this.email = email;
}
public String getPictureUrl() {
    return this.pictureUrl;
}
public void setPictureUrl(String pictureUrl) {
    this.pictureUrl = pictureUrl;
}
public Boolean getVerified() {
    return this.verified;
}
public void setVerified(Boolean verified) {
    this.verified = verified;
}
public String getToken() {
    return this.token;
}
public void setToken(String token) {
    this.token = token;
}
public long getTimeStamp() {
    return this.timeStamp;
}
public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
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
/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1635841394)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getAuthUserDao() : null;
}
}
