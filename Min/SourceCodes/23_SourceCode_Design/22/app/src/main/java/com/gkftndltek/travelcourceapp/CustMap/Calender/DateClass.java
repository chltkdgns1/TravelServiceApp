package com.gkftndltek.travelcourceapp.CustMap.Calender;

import java.io.Serializable;

public class DateClass implements Serializable {

    private int year,month,day;

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
}
