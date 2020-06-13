package com.example.sns;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class BitmapPostData {
    private String username;
    private String title;
    private String contents;
    private Uri video;
    private List<Bitmap> image;

    BitmapPostData(){
        image = new ArrayList<>();
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
    public Uri getVideo() {
        return video;
    }
    public void setVideo(Uri video) {
        this.video = video;
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
