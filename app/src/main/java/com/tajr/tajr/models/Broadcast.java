package com.tajr.tajr.models;

public class Broadcast {
    private String code;
    private String info;
    private String response;
    private String data;


    public Broadcast(String code, String info, String response, String data) {
        this.code = code;
        this.info = info;
        this.response = response;
        this.data = data;
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
}
