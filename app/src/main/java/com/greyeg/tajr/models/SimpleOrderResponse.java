package com.greyeg.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SimpleOrderResponse {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("data")
    @Expose
    private String data;

    @SerializedName("remainig_orders")
    @Expose
    private int remainig_orders;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("order_type")
    @Expose
    private String order_type;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("check_type")
    @Expose
    private String check_type;

    @SerializedName("order")
    @Expose
    private Order order;

    @SerializedName("cities")
    @Expose
    private List<City> cities;


    @SerializedName("user_id")
    @Expose
    private String user_id;

    public SimpleOrderResponse() {
    }

    public String getCheck_type() {
        return check_type;
    }

    public void setCheck_type(String check_type) {
        this.check_type = check_type;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getRemainig_orders() {
        return remainig_orders;
    }

    public void setRemainig_orders(int remainig_orders) {
        this.remainig_orders = remainig_orders;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
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

    public class Order {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("product_id")
        @Expose
        private String product_id;


        @SerializedName("product_name")
        @Expose
        private String product_name;

        @SerializedName("order_status")
        @Expose
        private String order_status;

        @SerializedName("order_status_type")
        @Expose
        private String order_status_type;

        @SerializedName("order_shipping_status")
        @Expose
        private String order_shipping_status;

        @SerializedName("order_shipping_status_type")
        @Expose
        private String order_shipping_status_type;

        @SerializedName("client_name")
        @Expose
        private String client_name;

        @SerializedName("client_address")
        @Expose
        private String client_address;

        @SerializedName("client_area")
        @Expose
        private String client_area;

        @SerializedName("client_city")
        @Expose
        private String client_city;

        @SerializedName("client_city_id")
        @Expose
        private String client_city_id;

        @SerializedName("phone_1")
        @Expose
        private String phone_1;

        @SerializedName("phone_2")
        @Expose
        private String phone_2;

        @SerializedName("item_cost")
        @Expose
        private String item_cost;

        @SerializedName("items_no")
        @Expose
        private String items_no;

        @SerializedName("shipping_cost")
        @Expose
        private String shipping_cost;

        @SerializedName("order_cost")
        @Expose
        private String order_cost;

        @SerializedName("total_order_cost")
        @Expose
        private String total_order_cost;

        @SerializedName("discount")
        @Expose
        private String discount;

        @SerializedName("fb_sender_id")
        @Expose
        private String fb_sender_id;

        @SerializedName("sender_name")
        @Expose
        private String sender_name;

        @SerializedName("notes")
        @Expose
        private String notes;

        @SerializedName("client_feedback")
        @Expose
        private String client_feedback;

        @SerializedName("order_type")
        @Expose
        private String order_type;

        @SerializedName("multi_orders")
        @Expose
        private List<Product> multi_orders;

        public Order() {
        }

        public List<Product> getMulti_orders() {
            return multi_orders;
        }

        public void setMulti_orders(List<Product> multi_orders) {
            this.multi_orders = multi_orders;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getOrder_status_type() {
            return order_status_type;
        }

        public void setOrder_status_type(String order_status_type) {
            this.order_status_type = order_status_type;
        }

        public String getOrder_shipping_status() {
            return order_shipping_status;
        }

        public void setOrder_shipping_status(String order_shipping_status) {
            this.order_shipping_status = order_shipping_status;
        }

        public String getOrder_shipping_status_type() {
            return order_shipping_status_type;
        }

        public void setOrder_shipping_status_type(String order_shipping_status_type) {
            this.order_shipping_status_type = order_shipping_status_type;
        }

        public String getClient_name() {
            return client_name;
        }

        public void setClient_name(String client_name) {
            this.client_name = client_name;
        }

        public String getClient_address() {
            return client_address;
        }

        public void setClient_address(String client_address) {
            this.client_address = client_address;
        }

        public String getClient_area() {
            return client_area;
        }

        public void setClient_area(String client_area) {
            this.client_area = client_area;
        }

        public String getClient_city() {
            return client_city;
        }

        public void setClient_city(String client_city) {
            this.client_city = client_city;
        }

        public String getClient_city_id() {
            return client_city_id;
        }

        public void setClient_city_id(String client_city_id) {
            this.client_city_id = client_city_id;
        }

        public String getPhone_1() {
            return phone_1;
        }

        public void setPhone_1(String phone_1) {
            this.phone_1 = phone_1;
        }

        public String getPhone_2() {
            return phone_2;
        }

        public void setPhone_2(String phone_2) {
            this.phone_2 = phone_2;
        }

        public String getItem_cost() {
            return item_cost;
        }

        public void setItem_cost(String item_cost) {
            this.item_cost = item_cost;
        }

        public String getItems_no() {
            return items_no;
        }

        public void setItems_no(String items_no) {
            this.items_no = items_no;
        }

        public String getShipping_cost() {
            return shipping_cost;
        }

        public void setShipping_cost(String shipping_cost) {
            this.shipping_cost = shipping_cost;
        }

        public String getOrder_cost() {
            return order_cost;
        }

        public void setOrder_cost(String order_cost) {
            this.order_cost = order_cost;
        }

        public String getTotal_order_cost() {
            return total_order_cost;
        }

        public void setTotal_order_cost(String total_order_cost) {
            this.total_order_cost = total_order_cost;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getFb_sender_id() {
            return fb_sender_id;
        }

        public void setFb_sender_id(String fb_sender_id) {
            this.fb_sender_id = fb_sender_id;
        }

        public String getSender_name() {
            return sender_name;
        }

        public void setSender_name(String sender_name) {
            this.sender_name = sender_name;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getClient_feedback() {
            return client_feedback;
        }

        public void setClient_feedback(String client_feedback) {
            this.client_feedback = client_feedback;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }
    }
}
