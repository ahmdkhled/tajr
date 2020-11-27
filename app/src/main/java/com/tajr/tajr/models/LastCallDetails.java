package com.tajr.tajr.models;

public class LastCallDetails {

    private String phone;
    private String duration;
    private String activeId;
    private String type;

    public LastCallDetails(String phone, String duration, String activeId, String type) {
        this.phone = phone;
        this.duration = duration;
        this.activeId = activeId;
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
