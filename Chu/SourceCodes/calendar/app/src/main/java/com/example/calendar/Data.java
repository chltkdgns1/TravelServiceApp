package com.example.calendar;

import java.io.Serializable;

public class Data implements Serializable {

    private int Data_year;
    private int Data_month;
    private int Data_day;

    public Data(int data_year, int data_month, int data_day) {
        Data_year = data_year;
        Data_month = data_month;
        Data_day = data_day;
    }

    public int getData_year() {
        return this.Data_year;
    }

    public void setData_year(int data_year) {
        this.Data_year = data_year;
    }

    public int getData_month() {
        return this.Data_month;
    }

    public void setData_month(int data_month) {
        this.Data_month = data_month;
    }

    public int getData_day() {
        return this.Data_day;
    }

    public void setData_day(int data_day) {
        this.Data_day = data_day;
    }
}
