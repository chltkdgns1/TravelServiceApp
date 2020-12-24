package com.gkftndltek.travelcourceapp.KakaoLog;

import java.io.Serializable;

public class UserData implements Serializable {
    private String nickName;
    private String email;
    private String imageName;

    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
