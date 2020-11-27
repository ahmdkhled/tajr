package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PointsHistory {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("response")
    @Expose
    private String response;


    @SerializedName("history")
    @Expose
    private List<Year> history;

    public PointsHistory() {
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

    public List<Year> getHistory() {
        return history;
    }

    public void setHistory(List<Year> history) {
        this.history = history;
    }

    public class Year{
        @SerializedName("year")
        @Expose
        private String year;

        @SerializedName("month")
        @Expose
        private List<Month> month;

        public Year() {
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public List<Month> getMonth() {
            return month;
        }

        public void setMonth(List<Month> month) {
            this.month = month;
        }

        public class Month{
            @SerializedName("name_en")
            @Expose
            private String name_en;

            @SerializedName("month")
            @Expose
            private String month;

            @SerializedName("name_ar")
            @Expose
            private String name_ar;

            @SerializedName("points")
            @Expose
            private String points;

            public Month() {
            }

            public String getName_en() {
                return name_en;
            }

            public void setName_en(String name_en) {
                this.name_en = name_en;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public String getName_ar() {
                return name_ar;
            }

            public void setName_ar(String name_ar) {
                this.name_ar = name_ar;
            }

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }
        }
    }

}
