package com.gkftndltek.travelcourceapp;

import java.io.Serializable;

public class UserData implements Serializable {
    private String nickName;

    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
