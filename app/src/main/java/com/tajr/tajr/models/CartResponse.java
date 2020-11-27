package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartResponse {

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
    private Data data;

    public CartResponse() {
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @SerializedName("number")
        @Expose
        private String number;

        @SerializedName("operator")
        @Expose
        private String operator;

        @SerializedName("price")
        @Expose
        private String price;

        @SerializedName("minutes")
        @Expose
        private String minutes;

        public Data() {
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMinutes() {
            return minutes;
        }

        public void setMinutes(String minutes) {
            this.minutes = minutes;
        }
    }
}
