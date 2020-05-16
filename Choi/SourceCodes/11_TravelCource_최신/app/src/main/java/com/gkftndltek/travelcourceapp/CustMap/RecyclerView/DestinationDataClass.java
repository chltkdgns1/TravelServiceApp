package com.gkftndltek.travelcourceapp.CustMap.RecyclerView;

import android.graphics.Bitmap;

public class DestinationDataClass {
    private String name; // title
    private String address;
    private String roadAddress;
    private String phone;
    private String category;
    private String description;
    private String url;
    private Bitmap link;
    private String key;
    private double x,y;
    private int drable;
    private int index;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Bitmap getLink() {
        return link;
    }
    public void setLink(Bitmap link) {
        this.link = link;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getRoadAddress() {
        return roadAddress;
    }
    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }
    public int getDrable() {
        return drable;
    }
    public void setDrable(int drable) {
        this.drable = drable;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
}
