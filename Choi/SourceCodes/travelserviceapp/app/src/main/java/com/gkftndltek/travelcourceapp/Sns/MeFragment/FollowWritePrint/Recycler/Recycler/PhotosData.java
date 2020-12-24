package com.gkftndltek.travelcourceapp.Sns.MeFragment.FollowWritePrint.Recycler.Recycler;

import android.graphics.Bitmap;

public class PhotosData {
    private String path;
    private int posts;
    private Bitmap data;

    public int getPosts() {
        return posts;
    }
    public void setPosts(int posts) {
        this.posts = posts;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public Bitmap getData() {
        return data;
    }
    public void setData(Bitmap data) {
        this.data = data;
    }
}
