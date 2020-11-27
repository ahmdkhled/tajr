package com.tajr.tajr.models;

public class CashHistoryData {

    private String cash;

    private String mobile;

    private String time;

    private String request_status;

    private String notes;

    public CashHistoryData() {
    }

    public CashHistoryData(String cash, String mobile, String time, String request_status, String notes) {
        this.cash = cash;
        this.mobile = mobile;
        this.time = time;
        this.request_status = request_status;
        this.notes = notes;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
