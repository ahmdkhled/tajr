package com.greyeg.tajr.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.crashlytics.android.Crashlytics;
import com.greyeg.tajr.R;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.models.AddReasonResponse;
import com.greyeg.tajr.models.CallTimePayload;
import com.greyeg.tajr.models.CallTimeResponse;
import com.greyeg.tajr.models.CancellationReasonsResponse;
import com.greyeg.tajr.models.MainResponse;
import com.greyeg.tajr.models.RemainingOrdersResponse;
import com.greyeg.tajr.models.UpdateOrderNewResponse;
import com.greyeg.tajr.models.CurrentOrderResponse;
import com.greyeg.tajr.repository.CallTimeRepo;
import com.greyeg.tajr.repository.CancellationReasonsRepository;
import com.greyeg.tajr.repository.OrdersRepo;
import com.greyeg.tajr.server.BaseClient;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class CurrentOrderViewModel extends AndroidViewModel {

    private MutableLiveData<CancellationReasonsResponse> cancellationReasonsResponse;
    private MutableLiveData<AddReasonResponse> addReason;
    private MutableLiveData<MainResponse> addReasonToOrder;
    private MutableLiveData<CurrentOrderResponse> currentOrder;
    private MutableLiveData<CallTimeResponse> callTime;

    private MutableLiveData<Boolean> isDelayedOrdersUpdating=new MutableLiveData<>();
    private MutableLiveData<String> delayedOrdersUpdatingError=new MutableLiveData<>();

    //remaining orders
    private MutableLiveData<Boolean> isRemainingOrdersLoading=new MutableLiveData<>();
    private MutableLiveData<String> remainingOrdersLoadingError=new MutableLiveData<>();

    private String token;

    public CurrentOrderViewModel(@NonNull Application application) {
        super(application);
        Log.d("CurrentOrderViewModel", "CurrentOrderViewModel: ");
        token=SessionManager.getInstance(application.getApplicationContext()).getToken();
    }


    //--------- cancellation reasons ------------
    public void getCancellationReasons(String token) {
        cancellationReasonsResponse= CancellationReasonsRepository.getInstance()
                .getCancellationReasons(token);
    }

    public MutableLiveData<CancellationReasonsResponse> getCancellationReasons() {
        return cancellationReasonsResponse;
    }

    public MutableLiveData<Boolean> getIsCancellationReasonsLoading(){
        return CancellationReasonsRepository.getInstance().getIsCancellationReasonsLoading();
    }

    public MutableLiveData<String> getCancellationReasonsLoadingError() {
        return CancellationReasonsRepository.getInstance().getCancellationReasonsLoadingError();
    }

    //submit new Order cancellation reason

    public MutableLiveData<AddReasonResponse> addReason(String token,String name){
        return addReason= CancellationReasonsRepository.getInstance()
                .addReason(token,name);
    }

    public MutableLiveData<AddReasonResponse> getAddReason() {
        return addReason;
    }

    public MutableLiveData<Boolean> getIsSubmittingReason() {
        return CancellationReasonsRepository.getInstance().getIsSubmittingReason();
    }

    public MutableLiveData<String> getReasonSubmittingError() {
        return CancellationReasonsRepository.getInstance().getReasonSubmittingError();
    }

    //---------- adding reason to order

    public void addReasonToOrder(String token, String orderId, String reason_id){
        addReasonToOrder=CancellationReasonsRepository.getInstance()
                .addReasonToOrder(token,orderId,reason_id);
    }

    public MutableLiveData<MainResponse> addReasonToOrder() {
        return addReasonToOrder;
    }

    public MutableLiveData<Boolean> getIsReasonAddingTOOrder() {
        return CancellationReasonsRepository.getInstance()
                .getIsReasonAddingTOOrder();
    }

    public MutableLiveData<String> getReasonAddingToOrderError() {
        return CancellationReasonsRepository.getInstance()
                .getReasonAddingToOrderError();
    }

    // ------- getting current order -------------
    public MutableLiveData<CurrentOrderResponse> getCurrentOrder(String token) {

        return currentOrder=OrdersRepo.getInstance().getCurrentOrder(token);
    }

    public MutableLiveData<CurrentOrderResponse> getCurrentOrder() {
        return currentOrder;
    }

    public MutableLiveData<Boolean> getIsCurrentOrderLoading() {
        return OrdersRepo.getInstance().getIsCurrentOrderLoading();
    }

    public MutableLiveData<String> getCurrentOrderLoadingError() {
        return OrdersRepo.getInstance().getCurrentOrderLoadingError();
    }

    //----------- upload call time  -----------------

    public MutableLiveData<CallTimeResponse>  setCallTime(CallTimePayload payload){
        callTime=new MutableLiveData<>();
        CallTimeRepo.getInstance()
                .setCallTime(payload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<CallTimeResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<CallTimeResponse> response) {
                        CallTimeResponse callTimeResponse=response.body();
                        if (response.isSuccessful()&&callTimeResponse!=null){
                            callTime.setValue(callTimeResponse);
                        }else {
                            Crashlytics.logException(new Throwable("Error parsing Call Time Response"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                    }
                });
        return callTime;
    }

    //---------- update order ------


    public MutableLiveData<UpdateOrderNewResponse> updateOrder(String token,
                                                               String order_id,
                                                               String user_id,
                                                               String status){

        return OrdersRepo.getInstance()
                .updateOrder(token,order_id,user_id,status);

    }

    public MutableLiveData<Boolean> getIsOrderUpdating() {
        return OrdersRepo.getInstance().getIsOrderUpdating();
    }

    public MutableLiveData<String> getOrderUpdatingError() {
        return OrdersRepo.getInstance().getOrderUpdateError();
    }

    //________________________________updateDelayedOrders_________________________________

    public MutableLiveData<UpdateOrderNewResponse> updateDelayedOrders(String order_id,
                                                                       String delayed_until,
                                                                       String user_id,
                                                                       String status){
        MutableLiveData<UpdateOrderNewResponse> update=new MutableLiveData<>();
        isDelayedOrdersUpdating.setValue(true);
        OrdersRepo.getInstance()
                .updateDelayedOrders(token,order_id,delayed_until,user_id,status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<UpdateOrderNewResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<UpdateOrderNewResponse> response) {
                        UpdateOrderNewResponse updateOrderNewResponse=response.body();
                        if (response.isSuccessful()&&updateOrderNewResponse!=null){
                            update.setValue(updateOrderNewResponse);
                        }else {
                            delayedOrdersUpdatingError.setValue(getApplication().getApplicationContext()
                            .getString(R.string.server_error));
                        }
                        isDelayedOrdersUpdating.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        delayedOrdersUpdatingError.setValue(getApplication().getApplicationContext()
                                .getString(R.string.server_error));
                        Crashlytics.logException(e);

                        isDelayedOrdersUpdating.setValue(false);
                    }
                });
        return update;
    }

    public MutableLiveData<Boolean> getIsDelayedOrdersUpdating() {
        return isDelayedOrdersUpdating;
    }

    public MutableLiveData<String> getDelayedOrdersUpdatingError() {
        return delayedOrdersUpdatingError;
    }

    //______________________________update progress_____________________

    public LiveData<RemainingOrdersResponse> getRemainingOrders(){
        MutableLiveData<RemainingOrdersResponse> remainingOrders=new MutableLiveData<>();
        isRemainingOrdersLoading.setValue(true);
        OrdersRepo
                .getInstance()
                .getRemainingOrders2(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<RemainingOrdersResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<RemainingOrdersResponse> response) {
                        RemainingOrdersResponse remainingOrdersResponse=response.body();
                        if (response.isSuccessful()&&remainingOrdersResponse!=null){
                            remainingOrders.setValue(remainingOrdersResponse);
                        }else {
                            remainingOrdersLoadingError.setValue(getApplication().getApplicationContext()
                                    .getString(R.string.error_parsing_remaining_orders));
                            Crashlytics.logException(new Throwable(getApplication().getApplicationContext()
                                    .getString(R.string.server_error)));
                        }
                        isRemainingOrdersLoading.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        remainingOrdersLoadingError.setValue(getApplication().getApplicationContext()
                        .getString(R.string.server_error));
                        Log.d("REMAININGGG", "onError: "+e.getMessage());
                        isRemainingOrdersLoading.setValue(true);
                        Crashlytics.logException(e);

                    }
                });
        return remainingOrders;
    }

    public MutableLiveData<Boolean> getIsRemainingOrdersLoading() {
        return isRemainingOrdersLoading;
    }

    public MutableLiveData<String> getRemainingOrdersLoadingError() {
        return remainingOrdersLoadingError;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("REASONORDER", "onCleared: ");
    }
}
