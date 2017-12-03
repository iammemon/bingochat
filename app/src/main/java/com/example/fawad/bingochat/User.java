package com.example.fawad.bingochat;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Fawad on 11/25/2017.
 */
@IgnoreExtraProperties
public class User {
    public String name;
    public String email;
    public String uid;
    public String avatar;

    public User(){
        //for firebase
    }
    public User(String name,String email,String uid,String avatar ){
        this.name=name;
        this.email=email;
        this.uid=uid;
        this.avatar=avatar;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
