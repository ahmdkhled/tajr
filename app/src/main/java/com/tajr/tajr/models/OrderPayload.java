package com.tajr.tajr.models;

import java.util.ArrayList;

public class OrderPayload {

     private String token;
     private String user_id;
     private String client_name;
     private String client_phone;
     private String city_id;
     private String client_area;
     private String client_address;
     private String discount;
     private String status_id;
     private String sender_name;
     private String sender_id;
     private ArrayList<OrderItem> order_body;


    public OrderPayload(String token, String user_id, String client_name,
                        String client_phone, String city_id, String client_area, String client_address,
                        String discount, String sender_name, String sender_id,
                        ArrayList<OrderItem> order_body) {
        this.token = token;
        this.user_id = user_id;
        this.client_name = client_name;
        this.client_phone = client_phone;
        this.city_id = city_id;
        this.client_area = client_area;
        this.client_address = client_address;
        this.discount = discount;
        this.sender_name = sender_name;
        this.sender_id = sender_id;
        this.order_body = order_body;
    }



    public OrderPayload() {
    }

    public String getToken() {
        return token;
    }

    public String getUser_id() {
        return user_id;
    }


    public String getClient_name() {
        return client_name;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getClient_area() {
        return client_area;
    }

    public String getClient_address() {
        return client_address;
    }



    public String getDiscount() {
        return discount;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public ArrayList<OrderItem> getOrder_body() {
        return order_body;
    }

    public String getStatus_id() {
        return status_id;
    }



}
