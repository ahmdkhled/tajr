package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultiOrderProducts {

    @SerializedName("extra_product_key")
    @Expose
    private int extra_product_key;

    @SerializedName("product_id")
    @Expose
    private String product_id;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("order_cost")
    @Expose
    private String order_cost;

    @SerializedName("order_phone")
    @Expose
    private String order_phone;

    @SerializedName("item_cost")
    @Expose
    private String item_cost;

    @SerializedName("items_no")
    @Expose
    private String items_no;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("extra_data")
    @Expose
    private List<String>extra_data;
    public MultiOrderProducts() {
    }

    public List<String> getExtra_data() {
        return extra_data;
    }

    public void setExtra_data(List<String> extra_data) {
        this.extra_data = extra_data;
    }

    public int getExtra_product_key() {
        return extra_product_key;
    }

    public void setExtra_product_key(int extra_product_key) {
        this.extra_product_key = extra_product_key;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getOrder_cost() {
        return order_cost;
    }

    public void setOrder_cost(String order_cost) {
        this.order_cost = order_cost;
    }

    public String getOrder_phone() {
        return order_phone;
    }

    public void setOrder_phone(String order_phone) {
        this.order_phone = order_phone;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "MultiOrderProducts{" +
                "extra_product_key=" + extra_product_key +
                ", product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", order_cost='" + order_cost + '\'' +
                ", order_phone='" + order_phone + '\'' +
                ", item_cost='" + item_cost + '\'' +
                ", items_no='" + items_no + '\'' +
                ", notes='" + notes + '\'' +
                ", extra_data=" + extra_data +
                '}';
    }
}
