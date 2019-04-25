package com.sadek.se7tak.model;

import java.io.Serializable;

public class Order implements Serializable {
    String id,  userId, DoctorId, phoneNumber, userName, orderTime, orderDate, examination, stuatus ;
    Object orderTimestamp;
    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(Object orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    public String getStuatus() {
        return stuatus;
    }

    public void setStuatus(String stuatus) {
        this.stuatus = stuatus;
    }
}

