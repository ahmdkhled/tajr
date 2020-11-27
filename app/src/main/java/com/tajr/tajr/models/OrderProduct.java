package com.tajr.tajr.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OrderProduct {

    private int key;
    private String id;
    private String name;
    private int price;
    private int items_no;
    private String image;
    private String cost;
    private ArrayList<ProductExtra> extras;

    public OrderProduct() {
    }

    public OrderProduct(String id, String name, int price, int items_no,
                        String image, String cost, ArrayList<ProductExtra> extras) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.items_no = items_no;
        this.image = image;
        this.cost = cost;
        this.extras = extras;
    }

    public OrderProduct(int items_no) {
        this.items_no = items_no;
    }

    public OrderProduct(String id) {
        this.id = id;
    }

    public int getKey() {
        return key;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getItems_no() {
        return items_no;
    }

    public String getImage() {
        return image;
    }

    public String getCost() {
        return cost;
    }

    public ArrayList<ProductExtra> getExtras() {
        return extras;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setItems_no(int items_no) {
        this.items_no = items_no;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setExtras(ArrayList<ProductExtra> extras) {
        this.extras = extras;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if ( !(obj instanceof OrderProduct) ) return false;
        OrderProduct orderProduct= (OrderProduct) obj;
        Log.d("UPDATEE", id+" equals: "+orderProduct.getId());
        return this.id.equals(orderProduct.getId());
    }

    @NonNull
    @Override
    public String toString() {
        return
                id+" "+
                name+" "+
                items_no+" ";
    }
}
