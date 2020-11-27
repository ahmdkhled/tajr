package com.tajr.tajr.models;

import java.util.ArrayList;

public class CallTimePayload {

    private String token;
    private String order_id;
    private ArrayList<CallActivity> activity;

    public CallTimePayload(String token, String order_id, ArrayList<CallActivity> activity) {
        this.token = token;
        this.order_id = order_id;
        this.activity = activity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public ArrayList<CallActivity> getActivity() {
        return activity;
    }

    public void setActivity(ArrayList<CallActivity> activity) {
        this.activity = activity;
    }
}
