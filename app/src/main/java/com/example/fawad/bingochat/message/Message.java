package com.example.fawad.bingochat.message;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Fawad on 11/17/2017.
 */
@IgnoreExtraProperties
public class Message {
    private String name;
    private String lastMessage;
    private String uid;
    private String avatar;

    public Message(){
        // firebase
    }
    public Message(String name, String message, String uid, String avatar){
        this.name=name;
        this.lastMessage=message;
        this.uid=uid;
        this.avatar=avatar;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
