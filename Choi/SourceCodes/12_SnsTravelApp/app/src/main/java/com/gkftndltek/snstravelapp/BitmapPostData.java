package com.gkftndltek.snstravelapp;

import android.graphics.Bitmap;

import java.io.Serializable;
public class BitmapPostData {
    private String username;
    private String title;
    private String contents;
    private String profile;
    private Bitmap url;

    public Bitmap getUrl() {
        return url;
    }
    public void setUrl(Bitmap url) {
        this.url = url;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContents() {
        return contents;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }
}
