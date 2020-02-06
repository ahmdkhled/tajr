package com.greyeg.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CurrentOrderResponse implements Serializable {

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

    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("remainig_orders")
    @Expose
    private Integer remainigOrders;
    @SerializedName("response_info")
    @Expose
    private String response_info;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("check_type")
    @Expose
    private String checkType;
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("cities")
    @Expose
    private List<City> cities = null;

    @SerializedName("history_line")
    @Expose
    private String history_line;

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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getRemainigOrders() {
        return remainigOrders;
    }

    public void setRemainigOrders(Integer remainigOrders) {
        this.remainigOrders = remainigOrders;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getHistory_line() {
        return history_line;
    }

    @Override
    public String toString() {
        return "CurrentOrderResponse{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", orderType='" + orderType + '\'' +
                ", remainigOrders=" + remainigOrders +
                ", response='" + response + '\'' +
                ", userId='" + userId + '\'' +
                ", checkType='" + checkType + '\'' +
                ", order=" + order +
                ", cities=" + cities +
                ", history_line=" + history_line +
                '}';
    }
}
