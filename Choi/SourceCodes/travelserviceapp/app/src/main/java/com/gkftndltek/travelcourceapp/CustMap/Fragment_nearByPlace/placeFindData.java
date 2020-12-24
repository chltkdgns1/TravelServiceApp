package com.gkftndltek.travelcourceapp.CustMap.Fragment_nearByPlace;

import android.graphics.Bitmap;

public class placeFindData {
    String addres;
    String title;
    String image;
    Bitmap bit;
    double x,y;

    public Bitmap getBit() {
        return bit;
    }
    public void setBit(Bitmap bit) {
        this.bit = bit;
    }
    public String getAddres() {
        return addres;
    }
    public void setAddres(String addres) {
        this.addres = addres;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
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
