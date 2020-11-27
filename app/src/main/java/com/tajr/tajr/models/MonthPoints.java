package com.tajr.tajr.models;

public class MonthPoints {

    private String month_name;
    private String cash;
    private String points;
    private String rank;

    public MonthPoints() {
    }

    public MonthPoints(String month_name, String cash, String points, String rank) {
        this.month_name = month_name;
        this.cash = cash;
        this.points = points;
        this.rank = rank;
    }

    public String getMonth_name() {
        return month_name;
    }

    public String getCash() {
        return cash;
    }

    public String getPoints() {
        return points;
    }

    public String getRank() {
        return rank;
    }
}
