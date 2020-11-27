package com.tajr.tajr.models;

import java.util.ArrayList;

public class ProductForSpinner {
    private String name;
    private String image;
    private String id;
    private String itemCost;
    private ArrayList<ProductExtra> extraData;
    public ProductForSpinner() {
    }

    public ProductForSpinner(String name, String image, String id, String itemCost) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.itemCost = itemCost;
    }

    public ProductForSpinner(String name, String image, String id, String itemCost, ArrayList<ProductExtra> extraData) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.itemCost = itemCost;
        this.extraData = extraData;
    }

    public String getItemCost() {
        return itemCost;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ProductExtra> getExtraData() {
        return extraData;
    }
}
