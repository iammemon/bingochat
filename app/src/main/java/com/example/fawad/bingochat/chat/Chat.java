package com.example.fawad.bingochat.chat;

/**
 * Created by Fawad on 11/17/2017.
 */

public class Chat {
    private String name;
    private String uid;
    private String avatar;
    private String message;
    private String date;

    public Chat(){
        //firebase
    }
    public Chat(String Name,String Uid,String Avatar,String Message,String Date){
        name=Name;
        uid=Uid;
        avatar=Avatar;
        message=Message;
        date=Date;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
