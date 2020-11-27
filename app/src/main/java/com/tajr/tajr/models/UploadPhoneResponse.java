package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadPhoneResponse {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("data")
    @Expose
    private String data;

    @SerializedName("response")
    @Expose
    private String response;

    public UploadPhoneResponse() {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "UploadPhoneResponse{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", data='" + data + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
