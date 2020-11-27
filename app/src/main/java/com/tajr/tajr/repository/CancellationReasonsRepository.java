package com.tajr.tajr.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.crashlytics.android.Crashlytics;
import com.tajr.tajr.models.AddReasonResponse;
import com.tajr.tajr.models.CancellationReasonsResponse;
import com.tajr.tajr.models.MainResponse;
import com.tajr.tajr.server.BaseClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancellationReasonsRepository {

    private static CancellationReasonsRepository cancellationReasonsRepository;

    //get order cancellation reason
    private MutableLiveData<CancellationReasonsResponse> cancellationReasonsResponse;
    private MutableLiveData<Boolean> isCancellationReasonsLoading=new MutableLiveData<>();
    private MutableLiveData<String> cancellationReasonsLoadingError=new MutableLiveData<>();

    //submit new Order cancellation reason
    private MutableLiveData<AddReasonResponse> addReasonResponse;
    private MutableLiveData<Boolean>  isSubmittingReason=new MutableLiveData<>();
    private MutableLiveData<String> reasonSubmittingError=new MutableLiveData<>();

    //add reason to order
    private MutableLiveData<MainResponse> addReasonToOrder;
    private MutableLiveData<Boolean>  isReasonAddingToOrder=new MutableLiveData<>();
    private MutableLiveData<String> reasonAddingToOrderError=new MutableLiveData<>();


    public static CancellationReasonsRepository getInstance() {
        return cancellationReasonsRepository ==null?cancellationReasonsRepository=new CancellationReasonsRepository(): cancellationReasonsRepository;
    }

    private CancellationReasonsRepository(){

    }

    public MutableLiveData<CancellationReasonsResponse> getCancellationReasons(String token){
        cancellationReasonsResponse=new MutableLiveData<>();
        isCancellationReasonsLoading.setValue(true);
        Log.d("CANCELLATIONN","from repo "+isCancellationReasonsLoading.getValue());
        BaseClient
                .getApiService()
                .getCancellationReasons(token)
                .enqueue(new Callback<CancellationReasonsResponse>() {
                    @Override
                    public void onResponse(Call<CancellationReasonsResponse> call, Response<CancellationReasonsResponse> response) {
                        isCancellationReasonsLoading.setValue(false);
                        Log.d("CANCELLATIONN","from repo "+isCancellationReasonsLoading.getValue());
                        if (response.isSuccessful()&&response.body()!=null)
                            cancellationReasonsResponse.setValue(response.body());
                        else
                            cancellationReasonsLoadingError.setValue("Error while getting cancellation error \n code"+response.code());
                    }

                    @Override
                    public void onFailure(Call<CancellationReasonsResponse> call, Throwable t) {
                        isCancellationReasonsLoading.setValue(false);
                        cancellationReasonsLoadingError.setValue(t.getMessage());
                        Crashlytics.logException(t);

                    }
                });

        return cancellationReasonsResponse;

    }

    public MutableLiveData<Boolean> getIsCancellationReasonsLoading() {
        Log.d("CANCELLATIONN","from repo getter "+(isCancellationReasonsLoading.getValue()));
        return isCancellationReasonsLoading;
    }

    public MutableLiveData<String> getCancellationReasonsLoadingError() {
        return cancellationReasonsLoadingError;
    }



    //submit new Order cancellation reason

    public MutableLiveData<AddReasonResponse> addReason(String token,String name){
        addReasonResponse=new MutableLiveData<>();
        isSubmittingReason.setValue(true);
        BaseClient
                .getApiService()
                .submitNewCancellationReason(token,name)
                .enqueue(new Callback<AddReasonResponse>() {
                    @Override
                    public void onResponse(Call<AddReasonResponse> call, Response<AddReasonResponse> response) {
                        if (response.isSuccessful()&&response.body()!=null)
                            addReasonResponse.setValue(response.body());
                        else
                            reasonSubmittingError.setValue("Error while submitting new Reason \n code"+response.code());


                        isSubmittingReason.setValue(false);

                    }

                    @Override
                    public void onFailure(Call<AddReasonResponse> call, Throwable t) {
                        isSubmittingReason.setValue(false);
                        reasonSubmittingError.setValue(t.getMessage());
                        Crashlytics.logException(t);

                    }
                });

        return addReasonResponse;
    }

    public MutableLiveData<Boolean> getIsSubmittingReason() {
        return isSubmittingReason;
    }

    public MutableLiveData<String> getReasonSubmittingError() {
        return reasonSubmittingError;
    }

    //add reason to order

    public synchronized MutableLiveData<MainResponse> addReasonToOrder(String token, String orderId, String reason_id){
        addReasonToOrder=new MutableLiveData<>();
        isReasonAddingToOrder.setValue(true);
        BaseClient
                .getApiService()
                .addReasonToOrder(token,orderId,reason_id)
                .enqueue(new Callback<MainResponse>() {
                    @Override
                    public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                        isReasonAddingToOrder.setValue(false);
                        MainResponse res=response.body();
                        //todo check code 1200 in all requests
                        if (response.isSuccessful()&&res!=null)
                            addReasonToOrder.setValue(res);
                        else
                            reasonAddingToOrderError.setValue("Error adding reason to order");
                    }

                    @Override
                    public void onFailure(Call<MainResponse> call, Throwable t) {
                        isReasonAddingToOrder.setValue(false);
                        reasonAddingToOrderError.setValue(t.getMessage());
                        Crashlytics.logException(t);

                        Log.d("REASONORDER", "onFailure: "+t.getMessage());

                    }
                });

        return addReasonToOrder;
    }

    public  MutableLiveData<Boolean> getIsReasonAddingTOOrder() {
        return isReasonAddingToOrder;
    }

    public MutableLiveData<String> getReasonAddingToOrderError() {
        return reasonAddingToOrderError;
    }
}
