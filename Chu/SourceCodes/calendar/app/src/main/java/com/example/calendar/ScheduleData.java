package com.example.calendar;

import java.io.Serializable;

public class ScheduleData implements Serializable {
    private String key;
    private String textview_schedulename;
    private String textview_schedulecontent;
    private String address;
    private String category;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() { return this.address; }
    public void setAddress(String address) { this.address = address; }

    public String getCategory() { return this.category; }
    public void setCategory(String category) { this.category = category; }


    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getTextview_schedulename() {
        return textview_schedulename;
    }
    public void setTextview_schedulename(String textview_schedulename) {
        this.textview_schedulename = textview_schedulename;
    }

    public ScheduleData(){}

    public String getTextview_schedulecontent() {
        return textview_schedulecontent;
    }

    public void setTextview_schedulecontent(String textview_schedulecontent) {
        this.textview_schedulecontent = textview_schedulecontent;
    }

    public ScheduleData(String textview_schedulename, String textview_schedulecontent) {
        this.textview_schedulename = textview_schedulename;
        this.textview_schedulecontent = textview_schedulecontent;
    }
}
