package com.gkftndltek.travelcourceapp.Sns.PostRecycle;

public class PostData {
    private String nickname;
    private String description;
    private String primaryKey;
    private int like;
    private long times;

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
    public String getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
