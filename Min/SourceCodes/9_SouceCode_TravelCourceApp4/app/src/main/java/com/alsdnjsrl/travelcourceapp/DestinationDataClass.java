package com.alsdnjsrl.travelcourceapp;

import android.graphics.Bitmap;

public class DestinationDataClass {
    private String name; // title
    private String address;
    private String roadAddress;
    private String phone;
    private String category;
    private String description;

    private Bitmap link;
    private double x,y;
    private int drable;

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
