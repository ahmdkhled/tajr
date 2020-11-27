package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllProducts {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("products_count")
    @Expose
    private int products_count;

    @SerializedName("pages")
    @Expose
    private Pages pages;

    @SerializedName("products")
    @Expose
    private List<ProductData> products;

    public AllProducts() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<ProductData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductData> products) {
        this.products = products;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public int getProducts_count() {
        return products_count;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setProducts_count(int products_count) {
        this.products_count = products_count;
    }

    public Pages getPages() {
        return pages;
    }

    @Override
    public String toString() {
        return "AllProducts{" +
                "code='" + code + '\'' +
                ", response='" + response + '\'' +
                ", info='" + info + '\'' +
                ", products_count=" + products_count +
                ", products=" + products +
                '}';
    }
}
