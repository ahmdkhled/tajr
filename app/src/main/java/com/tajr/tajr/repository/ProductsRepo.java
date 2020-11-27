package com.tajr.tajr.repository;

import com.tajr.tajr.models.AllProducts;
import com.tajr.tajr.server.BaseClient;

import io.reactivex.Single;
import retrofit2.Response;

public class ProductsRepo {

    private static ProductsRepo productsRepo;


    public static ProductsRepo getInstance() {
        return productsRepo==null?productsRepo=new ProductsRepo():productsRepo;
    }


    private ProductsRepo() {

    }

    public Single<Response<AllProducts>> getProducts(String token, String user_id,String page, String limit){
        Single<Response<AllProducts>> products
                = BaseClient
                .getApiService()
                .getProducts2(token,user_id,page,limit);

        return products;
    }




}
