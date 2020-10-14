package com.gkftndltek.travelcourceapp.Login;

import java.io.Serializable;

public class UserData implements Serializable {
    private String nickName;
    private String email;

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
