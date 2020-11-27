package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Order implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("order_status_type")
    @Expose
    private String orderStatusType;
    @SerializedName("order_shipping_status")
    @Expose
    private String orderShippingStatus;
    @SerializedName("order_shipping_status_type")
    @Expose
    private String orderShippingStatusType;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("client_address")
    @Expose
    private String clientAddress;
    @SerializedName("client_area")
    @Expose
    private String clientArea;
    @SerializedName("client_city")
    @Expose
    private String clientCity;
    @SerializedName("client_city_id")
    @Expose
    private String clientCityId;
    @SerializedName("phone_1")
    @Expose
    private String phone1;
    @SerializedName("phone_2")
    @Expose
    private String phone2;
    @SerializedName("item_cost")
    @Expose
    private String itemCost;
    @SerializedName("items_no")
    @Expose
    private String itemsNo;
    @SerializedName("shipping_cost")
    @Expose
    private String shippingCost;
    @SerializedName("order_cost")
    @Expose
    private String orderCost;
    @SerializedName("total_order_cost")
    @Expose
    private String totalOrderCost;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("fb_sender_id")
    @Expose
    private String fbSenderId;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("client_feedback")
    @Expose
    private String clientFeedback;
    @SerializedName("admin_reply")
    @Expose
    private String adminReply;
    @SerializedName("reply_time")
    @Expose
    private String replyTime;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("extra_data")
    @Expose
    private List<Map<String,String>> extraData;
    @SerializedName("multi_orders")
    @Expose
    private List<MultiOrderProducts> multiOrders = null;

    @SerializedName("products")
    @Expose
    private ArrayList<OrderProduct> products;

    @SerializedName("check_type")
    @Expose
    private String checkType;

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusType() {
        return orderStatusType;
    }

    public void setOrderStatusType(String orderStatusType) {
        this.orderStatusType = orderStatusType;
    }

    public String getOrderShippingStatus() {
        return orderShippingStatus;
    }

    public void setOrderShippingStatus(String orderShippingStatus) {
        this.orderShippingStatus = orderShippingStatus;
    }

    public String getOrderShippingStatusType() {
        return orderShippingStatusType;
    }

    public void setOrderShippingStatusType(String orderShippingStatusType) {
        this.orderShippingStatusType = orderShippingStatusType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientArea() {
        return clientArea;
    }

    public void setClientArea(String clientArea) {
        this.clientArea = clientArea;
    }

    public String getClientCity() {
        return clientCity;
    }

    public void setClientCity(String clientCity) {
        this.clientCity = clientCity;
    }

    public String getClientCityId() {
        return clientCityId;
    }

    public void setClientCityId(String clientCityId) {
        this.clientCityId = clientCityId;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getItemCost() {
        return itemCost;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public String getItemsNo() {
        return itemsNo;
    }

    public void setItemsNo(String itemsNo) {
        this.itemsNo = itemsNo;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getTotalOrderCost() {
        return totalOrderCost;
    }

    public void setTotalOrderCost(String totalOrderCost) {
        this.totalOrderCost = totalOrderCost;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFbSenderId() {
        return fbSenderId;
    }

    public void setFbSenderId(String fbSenderId) {
        this.fbSenderId = fbSenderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getClientFeedback() {
        return clientFeedback;
    }

    public void setClientFeedback(String clientFeedback) {
        this.clientFeedback = clientFeedback;
    }

    public String getAdminReply() {
        return adminReply;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<Map<String, String>> getExtraData() {
        return extraData;
    }

    public List<MultiOrderProducts> getMultiOrders() {
        return multiOrders;
    }

    public void setMultiOrders(List<MultiOrderProducts> multiOrders) {
        this.multiOrders = multiOrders;
    }

    public ArrayList<OrderProduct> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderStatusType='" + orderStatusType + '\'' +
                ", orderShippingStatus='" + orderShippingStatus + '\'' +
                ", orderShippingStatusType='" + orderShippingStatusType + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", clientArea='" + clientArea + '\'' +
                ", clientCity='" + clientCity + '\'' +
                ", clientCityId='" + clientCityId + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", itemCost='" + itemCost + '\'' +
                ", itemsNo='" + itemsNo + '\'' +
                ", shippingCost='" + shippingCost + '\'' +
                ", orderCost='" + orderCost + '\'' +
                ", totalOrderCost='" + totalOrderCost + '\'' +
                ", discount='" + discount + '\'' +
                ", fbSenderId='" + fbSenderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", notes='" + notes + '\'' +
                ", clientFeedback='" + clientFeedback + '\'' +
                ", adminReply='" + adminReply + '\'' +
                ", replyTime='" + replyTime + '\'' +
                ", orderType='" + orderType + '\'' +
                ", extraData=" + extraData +
                ", multiOrders=" + multiOrders +
                '}';
    }
}