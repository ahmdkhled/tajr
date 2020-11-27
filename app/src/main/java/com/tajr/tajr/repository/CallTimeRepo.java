package com.tajr.tajr.repository;

import com.tajr.tajr.models.CallTimePayload;
import com.tajr.tajr.models.CallTimeResponse;
import com.tajr.tajr.server.BaseClient;

import io.reactivex.Single;
import retrofit2.Response;

public class CallTimeRepo {

    private static CallTimeRepo callTimeRepo;

    public static CallTimeRepo getInstance() {
        return callTimeRepo==null?callTimeRepo=new CallTimeRepo():callTimeRepo;
    }

    public Single<Response<CallTimeResponse>> setCallTime(CallTimePayload callTimePayload){
        return BaseClient
                .getApiService()
                .setCallTime(callTimePayload);
    }
}
