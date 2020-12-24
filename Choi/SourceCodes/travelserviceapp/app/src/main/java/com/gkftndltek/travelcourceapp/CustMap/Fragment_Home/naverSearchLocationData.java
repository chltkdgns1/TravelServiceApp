package com.gkftndltek.travelcourceapp.CustMap.Fragment_Home;

import android.graphics.Bitmap;

public class naverSearchLocationData {
    private String title;
    private Bitmap link;
    private String category;
    private String description;
    private String tel;
    private String address;
    private String roadAddress;
    private String image;
    private boolean endPoint;
    private int index;

    private String url;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean getEndPoint() {
        return endPoint;
    }
    public void setEndPoint(boolean endPoint) {
        this.endPoint = endPoint;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Bitmap getLink() {
        return link;
    }
    public void setLink(Bitmap link) {
        this.link = link;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getRoadAddress() {
        return roadAddress;
    }
    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public void printAllData(){
        System.out.println("title : " + title);
        System.out.println("category : " + category);
        System.out.println("description : " + description);
        System.out.println("tel: " + tel);
        System.out.println("address : " + address);
        System.out.println("roadAddress : " + roadAddress);
    }

}
