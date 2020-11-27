package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductError {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("MainResponse")
    @Expose
    private String Response;

    @SerializedName("data")
    @Expose
    private String data;

    @SerializedName("errors")
    @Expose
    private String errors;

    public ProductError() {
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getResponse() {
        return Response;
    }

    public String getData() {
        return data;
    }

    public String getErrors() {
        return errors;
    }
}
