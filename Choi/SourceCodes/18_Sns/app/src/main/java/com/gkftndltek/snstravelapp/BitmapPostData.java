package com.gkftndltek.snstravelapp;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BitmapPostData {
    private String nickname;
    private String description;
    private List<Bitmap> image;

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BitmapPostData(){
        image = new ArrayList<>();
    }

    public Bitmap getImage(int idx){
        return image.get(idx);
    }
    public void setImage(Bitmap data){
        image.add(data);
    }
    public int getImageSize(){
        return image.size();
    }
}
