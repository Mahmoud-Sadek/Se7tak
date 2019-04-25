package com.sadek.se7tak.model;

import java.io.Serializable;

public class WorkDaysDoctor implements Serializable {
    String hourFrom, hourTo;
    int day;
    boolean holiday;
    public WorkDaysDoctor() {
    }


    public String getHourFrom() {
        return hourFrom;
    }

    public void setHourFrom(String hourFrom) {
        this.hourFrom = hourFrom;
    }

    public String getHourTo() {
        return hourTo;
    }

    public void setHourTo(String hourTo) {
        this.hourTo = hourTo;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }
}
