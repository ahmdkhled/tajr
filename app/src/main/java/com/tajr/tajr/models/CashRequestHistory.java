package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CashRequestHistory {


    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("response")
    @Expose
    private String  response;

    @SerializedName("total_available")
    @Expose
    private String  total_available;


    @SerializedName("data")
    @Expose
    private List<RequestData> data;

    public CashRequestHistory() {
    }

    public String getTotal_available() {
        return total_available;
    }

    public void setTotal_available(String total_available) {
        this.total_available = total_available;
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

    public List<RequestData> getData() {
        return data;
    }

    public void setData(List<RequestData> data) {
        this.data = data;
    }

    public class RequestData{

        @SerializedName("cash")
        @Expose
        private String cash;

        @SerializedName("mobile")
        @Expose
        private String mobile;

        @SerializedName("time")
        @Expose
        private String time;

        @SerializedName("request_status")
        @Expose
        private String request_status;

        @SerializedName("notes")
        @Expose
        private String notes;

        public RequestData() {
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRequest_status() {
            return request_status;
        }

        public void setRequest_status(String request_status) {
            this.request_status = request_status;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}
