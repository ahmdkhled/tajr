package com.greyeg.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SingleOrderProductsResponse implements Serializable {


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
    @SerializedName("products_count")
    @Expose
    private Integer productsCount;
    @SerializedName("products")
    @Expose
    private List<Product> products;

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

    public Integer getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(Integer productsCount) {
        this.productsCount = productsCount;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}