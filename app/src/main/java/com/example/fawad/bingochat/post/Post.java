package com.example.fawad.bingochat.post;

/**
 * Created by Fawad on 12/1/2017.
 */

public class Post {
    private String name;
    private String uid;
    private String email;
    private String avatar;
    private String title;
    private String summary;
    private String postImage;

    public Post(){
        //firebase
    }
    public Post(String name,String uid,String email,String avatar,String title,String summary,String postImage){
        this.name=name;
        this.uid=uid;
        this.email=email;
        this.avatar=avatar;
        this.title=title;
        this.summary=summary;
        this.postImage=postImage;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
}
