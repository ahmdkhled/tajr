package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class UpdateOrderNewResponse {


    @SerializedName("data")
    @Expose
    private String data;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("order_id")
    @Expose
    private String order_id;

    @SerializedName("history_line")
    @Expose
    private String history_line;

    public UpdateOrderNewResponse() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public String getOrder_id() {
        return order_id;
    }

    public String getHistory_line() {
        return history_line;
    }

    @NotNull
    @Override
    public String toString() {
        return "UpdateOrderNewResponse{" +
                "data='" + data + '\'' +
                ", code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
