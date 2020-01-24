package com.greyeg.tajr.repository;

import com.greyeg.tajr.order.models.CurrentOrderResponse;
import com.greyeg.tajr.server.BaseClient;

import io.reactivex.Single;
import retrofit2.Response;

public class PhoneRepo {

    private static PhoneRepo phoneRepo;

    public static PhoneRepo getInstance() {
        return phoneRepo==null?phoneRepo=new PhoneRepo():phoneRepo;
    }

    public Single<Response<CurrentOrderResponse>> getPhoneData(String token, String userId, String phone){
        return BaseClient
                .getApiService()
                .getPhoneData3(token,userId,phone);
    }
}
