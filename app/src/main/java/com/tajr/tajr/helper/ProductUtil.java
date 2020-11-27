package com.tajr.tajr.helper;

import com.tajr.tajr.models.OrderProduct;
import com.tajr.tajr.models.ProductData;
import com.tajr.tajr.models.ProductExtra;

import java.util.HashMap;

public class ProductUtil {

    public static OrderProduct toOrderProduct(ProductData productData,OrderProduct product){
        if (productData==null)return null;
        product.setId(productData.getProduct_id());
        product.setName(productData.getProduct_name());
        product.setPrice(Integer.parseInt(productData.getProduct_real_price()));
        product.setImage(productData.getProduct_image());
        product.setExtras(productData.getExtra_data());
        return product;

    }

    public static OrderProduct setExtraDataValues(OrderProduct product , HashMap<String,Object> values ){
        for (String key: values.keySet()) {
            product.getExtras().get(product.getExtras()
                    .indexOf(new ProductExtra(key)))
                    .setValue((String) values.get(key));
        }
        return product;
    }
}
