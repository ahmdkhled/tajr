package com.greyeg.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.greyeg.tajr.models.ExtraData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Product implements Serializable {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("product_describtion")
    @Expose
    private String productDescribtion;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_info")
    @Expose
    private String productInfo;
    @SerializedName("product_real_price")
    @Expose
    private String productRealPrice;

    @SerializedName("extra_data")
    private ArrayList<ExtraData> extra_data;

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getProductDescribtion() {
        return productDescribtion;
    }

    public void setProductDescribtion(String productDescribtion) {
        this.productDescribtion = productDescribtion;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getProductRealPrice() {
        return productRealPrice;
    }

    public void setProductRealPrice(String productRealPrice) {
        this.productRealPrice = productRealPrice;
    }

    public ArrayList<ExtraData> getExtra_data() {
        return extra_data;
    }
}