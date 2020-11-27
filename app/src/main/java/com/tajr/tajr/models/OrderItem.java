package com.tajr.tajr.models;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class OrderItem {

    private String product_id;
    private int items;
    private HashMap<String,Object> extras;


    public OrderItem(String product_id, int items) {
        this.product_id = product_id;
        this.items = items;
    }

    public OrderItem(String product_id, int items, HashMap<String, Object> extras) {
        this.product_id = product_id;
        this.items = items;
        this.extras = extras;
    }

    public OrderItem(String product_id) {
        this.product_id = product_id;
    }

    public OrderItem() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public int getItems() {
        return items;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public HashMap<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(HashMap<String, Object> extras) {
        this.extras = extras;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj==null)return false;
        if (!(obj instanceof OrderItem)) return false;
        OrderItem orderItem= (OrderItem) obj;

        return orderItem.product_id.equals(this.product_id);
    }
}
