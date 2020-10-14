package com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler;

import java.io.Serializable;

public class CalenderDataClass implements Serializable {
    private String name;
    private String content;
    private String key;
    private String url;
    private String category;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }


}
