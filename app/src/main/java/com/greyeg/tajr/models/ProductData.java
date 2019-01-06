package com.greyeg.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private String extra_data;

    public ProductData() {
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

    public String getExtra_data() {
        return extra_data;
    }
}
