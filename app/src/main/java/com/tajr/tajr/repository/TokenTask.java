package com.tajr.tajr.repository;


import android.content.Context;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TokenTask  {

    public static Single<String> getToken(Context context, GoogleSignInAccount account,String scope){
        return Single
                .fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String token= GoogleAuthUtil.getToken(context,account.getAccount(),scope);

                return token;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


}
