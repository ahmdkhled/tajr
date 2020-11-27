package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemainingOrdersResponse {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("data")
    @Expose
    private int data;

    public RemainingOrdersResponse() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RemainingOrdersResponse{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", response='" + response + '\'' +
                ", data=" + data +
                '}';
    }
}
