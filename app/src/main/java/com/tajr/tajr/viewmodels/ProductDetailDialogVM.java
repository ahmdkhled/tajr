package com.tajr.tajr.viewmodels;


import androidx.lifecycle.ViewModel;
import com.tajr.tajr.models.OrderProduct;
import com.tajr.tajr.view.dialogs.ProductDetailDialog;

public class ProductDetailDialogVM extends ViewModel {

     private OrderProduct product;
     private ProductDetailDialog.OnProductUpdated onProductUpdated;



    public OrderProduct getProduct() {
        return product;
    }

    public void setProduct(OrderProduct product) {
        this.product = product;
    }

    public ProductDetailDialog.OnProductUpdated getOnProductUpdated() {
        return onProductUpdated;
    }

    public void setOnProductUpdated(ProductDetailDialog.OnProductUpdated onProductUpdated) {
        this.onProductUpdated = onProductUpdated;
    }
}
