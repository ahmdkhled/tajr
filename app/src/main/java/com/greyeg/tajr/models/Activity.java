package com.greyeg.tajr.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;

public class Activity {

    private long value;
    private long stamp;
    private String type;
    @Expose(deserialize = false)
    private String status="calculating";

    public Activity(long value, long stamp, String type) {
        this.value = value;
        this.stamp = stamp;
        this.type = type;
    }

    public Activity(long value, long stamp, String type, String status) {
        this.value = value;
        this.stamp = stamp;
        this.type = type;
        this.status = status;
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

    public void setValue(long value) {
        this.value = value;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "stamp "+stamp+ " >>> "+value+" >>> "+type+" -- status "+status ;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj==null)return false;
        if (!(obj instanceof Activity))return false;
        Activity activity= (Activity) obj;
        return  (activity.stamp==this.stamp);
    }
}
