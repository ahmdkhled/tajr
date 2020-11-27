package com.tajr.tajr.models;

import java.util.ArrayList;

public class CancellationReasonsResponse {

    private String code;
    private String info;
    private String response;
    private String data;
    private int reasons_count;
    private ArrayList<CancellationReason> reasons;

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

    public int getReasons_count() {
        return reasons_count;
    }

    public ArrayList<CancellationReason> getReasons() {
        return reasons;
    }
}
