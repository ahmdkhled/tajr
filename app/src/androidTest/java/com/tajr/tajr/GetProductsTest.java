package com.tajr.tajr;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import android.util.Log;

import com.tajr.tajr.models.AllProducts;
import com.tajr.tajr.server.Api;
import com.tajr.tajr.server.BaseClient;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;

public class GetProductsTest {

    private static final String TAG = "GetProductsTest";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.greyeg.tajr", appContext.getPackageName());
    }

    @Test
    public void getProducts() {
        BaseClient.getBaseClient().create(Api.class).getProducts("YIXRKEsDUv4VpAP5BaroqlJb", "108")
                .enqueue(new Callback<AllProducts>() {
                    @Override
                    public void onResponse(Call<AllProducts> call, Response<AllProducts> response) {
                        System.out.println(response.body().getCode());
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<AllProducts> call, Throwable t) {
                        System.out.println(t.getMessage());
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }
}


