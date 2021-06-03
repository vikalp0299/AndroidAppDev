package com.example.connect.Entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Objects;

@Entity
public class RoomMember {
    @Id(autoincrement = true)
    private Long id;

    @Index
    private String mid;

    @Index
    public String name;

    public String pictureUrl;

    @Index
    public String email;

    @Index
    public String rid;

    @Generated(hash = 1680104556)
    public RoomMember(Long id, String mid, String name, String pictureUrl,
            String email, String rid) {
        this.id = id;
        this.mid = mid;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.email = email;
        this.rid = rid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof String)) return false;
        return getMid().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMid());
    }

    @Generated(hash = 538897564)
    public RoomMember() {
    }

    public Long getId() {
        return this.id;
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRid() {
        return this.rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
