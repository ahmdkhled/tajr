package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToalAvailableBalance {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("query_type")
    @Expose
    private Object queryType;
    @SerializedName("balance")
    @Expose
    private Double balance;
    @SerializedName("total_available_balance")
    @Expose
    private Double totalAvailableBalance;

    @SerializedName("data")
    @Expose
    private String data;

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

    public Object getQueryType() {
        return queryType;
    }

    public void setQueryType(Object queryType) {
        this.queryType = queryType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getTotalAvailableBalance() {
        return totalAvailableBalance;
    }

    public void setTotalAvailableBalance(Double totalAvailableBalance) {
        this.totalAvailableBalance = totalAvailableBalance;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ToalAvailableBalance{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", response='" + response + '\'' +
                ", queryType=" + queryType +
                ", balance=" + balance +
                ", totalAvailableBalance=" + totalAvailableBalance +
                ", data='" + data + '\'' +
                '}';
    }
}