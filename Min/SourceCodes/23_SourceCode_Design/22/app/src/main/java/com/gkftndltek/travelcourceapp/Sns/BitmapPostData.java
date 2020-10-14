package com.gkftndltek.travelcourceapp.Sns;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BitmapPostData {
    private String nickname;
    private String description;
    private List<Bitmap> image; // 게시글에 사진
    // private List<PostComment> comment;
    private String nowNick; //
    private String path;
    private int like; // 좋아요
    private long times; // 몇시간

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getNowNick() {
        return nowNick;
    }
    public void setNowNick(String nowNick) {
        this.nowNick = nowNick;
    }
    public long getTimes() {
        return times;
    }
    public void setTimes(long times) {
        this.times = times;
    }
    public int getLike() {
        return like;
    }
    public void setLike(int like) {
        this.like = like;
    }
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
