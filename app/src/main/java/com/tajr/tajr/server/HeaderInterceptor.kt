package com.tajr.tajr.server

import android.util.Log
import com.tajr.tajr.helper.App
import com.tajr.tajr.helper.SharedHelper
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    private  val TAG = "HeaderInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val access_token=SharedHelper.getKey(App.getContext(),"access_token")
        Log.d(TAG, "intercept: "+chain.request().url.host)

        if (chain.request().url.host.contains("googleapis")){
            Log.d(TAG, "contains: "+chain.request().url)
                    return chain.proceed(chain.request()
                            .newBuilder()
                            .addHeader("Authorization","Bearer "+access_token)
                            .build())


        }
        return chain.proceed(chain.request())
    }
}