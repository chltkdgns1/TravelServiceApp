package com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow.Recycler;

import android.graphics.Bitmap;

public class FriendsDataClass {
    private String uid;
    private String uuid;
    private String username;
    private Bitmap imageUrl;
    private int imageName;

    public Bitmap getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(Bitmap imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getImageName() {
        return imageName;
    }
    public void setImageName(int imageName) {
        this.imageName = imageName;
    }
}
