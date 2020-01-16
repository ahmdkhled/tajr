package com.greyeg.tajr.repository;

import com.greyeg.tajr.models.BotBlocksResponse;
import com.greyeg.tajr.models.Broadcast;
import com.greyeg.tajr.server.BaseClient;

import io.reactivex.Single;
import retrofit2.Response;

public class BotBlocksRepo {

    private static BotBlocksRepo botBlocksRepo;


    public static BotBlocksRepo getInstance() {
        return botBlocksRepo == null ? botBlocksRepo = new BotBlocksRepo():botBlocksRepo;
    }

    public Single<Response<BotBlocksResponse>> getBotBlocks(String token){
        return BaseClient
                .getApiService()
                .getBotBlocks(token);
    }

    public Single<Response<Broadcast>> sendBroadcast(String token,String subscriber,String page,String block){
        return BaseClient
                .getApiService()
                .sendBroadcast(token,subscriber,page,block);
    }
}
