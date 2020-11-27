package com.tajr.tajr.enums;

public enum OrderProductsType {

    SingleOrder("single order"),
    MuhltiOrder("multi order");

    public String type;

    OrderProductsType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

}
