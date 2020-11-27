package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Points {


    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("response")
    @Expose
    private int response;

    @SerializedName("data")
    @Expose
    private Data data;

    public Points() {
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public int getResponse() {
        return response;
    }

    public Data getData() {
        return data;
    }

    public class Data{


        @SerializedName("cash")
        @Expose
        private String cash;

        @SerializedName("points")
        @Expose
        private String points;

        @SerializedName("rank")
        @Expose
        private int rank;

        public Data() {
        }

        public Data(String cash, String points, int rank) {
            this.cash = cash;
            this.points = points;
            this.rank = rank;
        }
    }
}
