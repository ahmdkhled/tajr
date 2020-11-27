package com.tajr.tajr.models;

import java.util.ArrayList;

public class SubscriberInfo {

    private String code;
    private String info;
    private String response;
    private String data;
    private ArrayList<Subscriber> subscribers_data;


    public SubscriberInfo(String code, String info, String response
            , String data,ArrayList<Subscriber> subscribers_data) {
        this.code = code;
        this.info = info;
        this.response = response;
        this.data = data;
        this.subscribers_data = subscribers_data;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getResponse() {
        return response;
    }

    public String getData() {
        return data;
    }

    public ArrayList<Subscriber> getSubscribers_data() {
        return subscribers_data;
    }
}
