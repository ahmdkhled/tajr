package com.tajr.tajr.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseClient {
    private static BaseClient baseClient;
    private static Retrofit retrofit = null;
    private static Api  apiService;

    public static BaseClient getInstance() {
        return baseClient==null?baseClient=new BaseClient():baseClient;
    }

    private static OkHttpClient buildClient() {
        return new OkHttpClient
                .Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new HeaderInterceptor())
                .authenticator(new TokenAuthenticator())
                .build();
    }

    public static Retrofit getBaseClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(buildClient())

                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("https://tajr.xyz/")
                    .build();
        }
        return retrofit;
    }

    public static Api getApiService(){
        return apiService==null?apiService= getBaseClient().create(Api.class):apiService;
    }

}
