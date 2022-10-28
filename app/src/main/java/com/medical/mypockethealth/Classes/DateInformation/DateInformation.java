package com.medical.mypockethealth.Classes.DateInformation;

import java.io.Serializable;

public class DateInformation implements Serializable {

    private String date;
    private String day;
    private String month;
    private String year;

    public DateInformation(String date, String day, String month, String year) {
        this.date = date;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }
}
