package com.example.sns;

public class CommentData {

    private int comment_profile;
    private String comment_username;
    private String comment_contents;

    public CommentData(int comment_profile, String comment_username, String comment_contents) {
        this.comment_profile = comment_profile;
        this.comment_username = comment_username;
        this.comment_contents = comment_contents;
    }
    public int getComment_profile() {
        return comment_profile;
    }
    public void setComment_profile(int comment_profile) {
        this.comment_profile = comment_profile;
    }
    public String getComment_username() {
        return comment_username;
    }
    public void setComment_username(String comment_username) {
        this.comment_username = comment_username;
    }
    public String getComment_contents() {
        return comment_contents;
    }
    public void setComment_contents(String comment_contents) {
        this.comment_contents = comment_contents;
    }
}
