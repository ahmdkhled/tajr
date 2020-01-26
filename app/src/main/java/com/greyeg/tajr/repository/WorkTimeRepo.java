package com.greyeg.tajr.repository;

import androidx.lifecycle.MutableLiveData;

import com.crashlytics.android.Crashlytics;
import com.greyeg.tajr.models.MainResponse;
import com.greyeg.tajr.models.UserTimePayload;
import com.greyeg.tajr.models.UserWorkTimeResponse;
import com.greyeg.tajr.server.BaseClient;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkTimeRepo {

    private static WorkTimeRepo workTimeRepo;
    private MutableLiveData<UserWorkTimeResponse> sendWorkTime;
    private MutableLiveData<Boolean> isWorkTimeSending;
    private MutableLiveData<String> workTimeSendingError;


    public static WorkTimeRepo getInstance() {
        return workTimeRepo == null ?workTimeRepo= new WorkTimeRepo() : workTimeRepo;
    }

    public MutableLiveData<UserWorkTimeResponse> sendWorkTime( String token,
                                                                String activity,
                                                                String user_id,
                                                                String action){
        sendWorkTime=new MutableLiveData<>();
        isWorkTimeSending=new MutableLiveData<>();
        workTimeSendingError=new MutableLiveData<>();

        isWorkTimeSending.setValue(true);
        BaseClient
                .getApiService()
                .userWorkTime(token,activity,user_id,action)
                .enqueue(new Callback<UserWorkTimeResponse>() {
                    @Override
                    public void onResponse(Call<UserWorkTimeResponse> call, Response<UserWorkTimeResponse> response) {
                        UserWorkTimeResponse userWorkTimeResponse=response.body();
                        if (userWorkTimeResponse!=null)
                            sendWorkTime.setValue(userWorkTimeResponse);
                        else
                            workTimeSendingError.setValue("server Error");

                        isWorkTimeSending.setValue(false);
                    }

                    @Override
                    public void onFailure(Call<UserWorkTimeResponse> call, Throwable t) {
                        isWorkTimeSending.setValue(false);
                        workTimeSendingError.setValue(t.getMessage());
                        Crashlytics.logException(t);


                    }
                });


        return sendWorkTime;


    }

    public Single<Response<UserWorkTimeResponse>> sendWorkTime2(String token,
                                                                String activity,
                                                                String user_id,
                                                                String action){

        return BaseClient
                .getApiService()
                .sendWorkTime(token,activity,user_id,action);
    }

    public MutableLiveData<Boolean> getIsWorkTimeSending() {
        return isWorkTimeSending;
    }

    public MutableLiveData<String> getWorkTimeSendingError() {
        return workTimeSendingError;
    }

    public Single<Response<MainResponse>> setUserTime(UserTimePayload userTimePayload){
        return BaseClient.getApiService()
                .set_user_time(userTimePayload);
    }


}
