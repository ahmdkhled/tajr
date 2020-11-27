package com.tajr.tajr;

import android.util.Log;

import com.tajr.tajr.models.CardsResponse;
import com.tajr.tajr.server.Api;
import com.tajr.tajr.server.BaseClient;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailableCardsTest {

    private static final String TAG = "AvailableCardsTest";

    @Test
    public void availableCardsTest() {
        BaseClient.getBaseClient().create(Api.class).getCards("YIXRKEsDUv4VpAP5BaroqlJb")
                .enqueue(new Callback<CardsResponse>() {
                    @Override
                    public void onResponse(Call<CardsResponse> call, Response<CardsResponse> response) {
                        System.out.println(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<CardsResponse> call, Throwable t) {
                        System.out.println(t.getMessage());
                        Log.d(TAG, "onResponse: " + t.getMessage());

                    }
                });
    }
}
