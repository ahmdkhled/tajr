package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteAddProductResponse {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("Details")
    @Expose
    private String Details;

    @SerializedName("data")
    @Expose
    private String data;

    public DeleteAddProductResponse() {
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

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DeleteAddProductResponse{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", response='" + response + '\'' +
                ", Details='" + Details + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
