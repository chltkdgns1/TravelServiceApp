package com.gkftndltek.snstravelapp;

import java.io.Serializable;

public class PostData {
    private String username;
    private String title;
    private String contents;
    private String filePathVideo;

    public String getFilePathVideo() {
        return filePathVideo;
    }
    public void setFilePathVideo(String filePathVideo) {
        this.filePathVideo = filePathVideo;
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
}
