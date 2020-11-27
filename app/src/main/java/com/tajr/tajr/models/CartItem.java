package com.tajr.tajr.models;

import androidx.annotation.Nullable;

public class CartItem {

    private ProductData product;
    private int quantity;

    public CartItem(ProductData product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public CartItem(String productId) {
        this.product = new ProductData(productId);
    }

    public ProductData getProduct() {
        return product;
    }

    public void setProduct(ProductData product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj==null) return false;
        if (!(obj instanceof CartItem)) return false;
        CartItem cartItem= (CartItem) obj;
        return cartItem.product.equals(this.product);
    }
}
