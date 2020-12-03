package com.tajr.tajr.server;

import android.content.Context;
import android.util.Log;
import com.tajr.tajr.R;
import com.tajr.tajr.helper.App;
import com.tajr.tajr.helper.SharedHelper;
import com.tajr.tajr.models.AccessTokenRes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {



    private String TOKEN_URL="https://accounts.google.com/o/oauth2/token";
    private static final String TAG = "TokenAuthenticator";

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) {
        Log.d(TAG, "authenticate: "+response.code());
        Log.d(TAG, "authenticate: "+response.request().url().toString());
        if (!"www.googleapis.com".equals(route.address().url().host()))
            return response.request();
        AccessTokenRes accessTokenRes=getUpdateToken();
        if (accessTokenRes!=null&&accessTokenRes.getAccess_token()!=null){
            SharedHelper.putKey(App.getContext(),"access_token",accessTokenRes.getAccess_token());
        }
        Log.d(TAG, "authenticate: "+accessTokenRes.toString());
        return response.request().newBuilder()
                .header("Authorization", "Bearer "+accessTokenRes.getAccess_token())
                .build();

    }

    AccessTokenRes getUpdateToken(){
        Context context= App.getContext();
        String refreshToken= SharedHelper.getKey(context,"refresh_token");
        String client_id=context.getString(R.string.client_id);
        String client_secret=context.getString(R.string.client_secret);

        HashMap<String,String> fields=new HashMap<>();
        fields.put("grant_type","refresh_token");
        fields.put("refresh_token",refreshToken);
        fields.put("client_id",client_id);
        fields.put("client_secret",client_secret);

        Log.d(TAG, "getUpdateToken: "+refreshToken);
        try {
            return BaseClient
                    .getApiService()
                    .getAccessToken(TOKEN_URL,fields)
                    .execute()
                    .body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
