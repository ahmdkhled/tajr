package com.greyeg.tajr.models;

public class Activity {

    private long value;
    private long stamp;
    private String type;

    public Activity(long value, long stamp, String type) {
        this.value = value;
        this.stamp = stamp;
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public long getStamp() {
        return stamp;
    }

    public String getType() {
        return type;
    }
}
