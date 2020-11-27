package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateOrderResponse {

    @SerializedName("problem_id")
    @Expose
    private String problem_id;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("order")
    @Expose
    private Order order;

    @SerializedName("notes")
    @Expose
    private Order notes;

    public UpdateOrderResponse() {
    }

    public String getProblem_id() {
        return problem_id;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getNotes() {
        return notes;
    }

    public void setNotes(Order notes) {
        this.notes = notes;
    }
}
