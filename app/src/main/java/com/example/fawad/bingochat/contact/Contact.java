package com.example.fawad.bingochat.contact;

/**
 * Created by Fawad on 11/19/2017.
 */

public class Contact {

    private String name;
    private String uid;
    private String avatar;
    private String email;


    public Contact(){
        // firebase
    }
    public Contact(String name, String email, String uid, String avatar){
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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
