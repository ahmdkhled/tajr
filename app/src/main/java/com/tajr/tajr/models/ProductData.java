package com.tajr.tajr.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductData {

    @SerializedName("product_id")
    @Expose
    private String product_id;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("category_id")
    @Expose
    private String category_id;

    @SerializedName("category_name")
    @Expose
    private String category_name;

    @SerializedName("sub_category_id")
    @Expose
    private String sub_category_id;

    @SerializedName("sub_category_name")
    @Expose
    private String sub_category_name;


    @SerializedName("product_describtion")
    @Expose
    private String product_describtion;

    @SerializedName("product_image")
    @Expose
    private String product_image;

    @SerializedName("product_info")
    @Expose
    private String product_info;

    @SerializedName("product_real_price")
    @Expose
    private String product_real_price;

    @SerializedName("extra_data")
    @Expose
    private ArrayList<ProductExtra> extra_data;

    public ProductData() {
    }

    public ProductData(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public void setProduct_describtion(String product_describtion) {
        this.product_describtion = product_describtion;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }

    public void setProduct_real_price(String product_real_price) {
        this.product_real_price = product_real_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public String getProduct_describtion() {
        return product_describtion;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getProduct_info() {
        return product_info;
    }

    public String getProduct_real_price() {
        return product_real_price;
    }

    public ArrayList<ProductExtra> getExtra_data() {
        return extra_data;
    }

    @Override
    public String toString() {
        return "ProductData{" +
                "product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", category_id='" + category_id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", sub_category_id='" + sub_category_id + '\'' +
                ", sub_category_name='" + sub_category_name + '\'' +
                ", product_describtion='" + product_describtion + '\'' +
                ", product_image='" + product_image + '\'' +
                ", product_info='" + product_info + '\'' +
                ", product_real_price='" + product_real_price + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj==null)return false;
        if (!(obj instanceof ProductData)) return false;
        ProductData productData= (ProductData) obj;
        return productData.product_id.equals(this.product_id);
    }
}
