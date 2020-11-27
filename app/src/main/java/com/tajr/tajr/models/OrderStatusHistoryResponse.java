package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderStatusHistoryResponse {

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
    private String data;

    @SerializedName("history")
    @Expose
    private List<History> historyList;


    public OrderStatusHistoryResponse() {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
    }

    @Override
    public String toString() {
        return "OrderStatusHistoryResponse{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", response='" + response + '\'' +
                ", data='" + data + '\'' +
                ", historyList=" + historyList +
                '}';
    }

    public class History {
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("source")
        @Expose
        private String source;

        @SerializedName("date")
        @Expose
        private String date;

        public History() {
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "History{" +
                    "status='" + status + '\'' +
                    ", source='" + source + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }
    }
}
