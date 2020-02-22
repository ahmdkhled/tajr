package com.greyeg.tajr.repository;

import android.content.res.Resources;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.crashlytics.android.Crashlytics;
import com.greyeg.tajr.helper.App;
import com.greyeg.tajr.R;
import com.greyeg.tajr.models.NewOrderResponse;
import com.greyeg.tajr.models.OrderPayload;
import com.greyeg.tajr.models.UpdateOrderNewResponse;
import com.greyeg.tajr.models.CurrentOrderResponse;
import com.greyeg.tajr.server.BaseClient;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersRepo {

    private static OrdersRepo ordersRepo;

    //--------- current order -------------------------
    private MutableLiveData<CurrentOrderResponse> currentOrder;
    private MutableLiveData<Boolean> isCurrentOrderLoading=new MutableLiveData<>();
    private MutableLiveData<String> currentOrderLoadingError=new MutableLiveData<>();

    //------------- order status ----------------

    private MutableLiveData<UpdateOrderNewResponse> updateOrder;
    private MutableLiveData<Boolean> isOrderUpdating=new MutableLiveData<>();
    private MutableLiveData<String> orderUpdateError=new MutableLiveData<>();

    public static OrdersRepo getInstance() {
        return ordersRepo==null?ordersRepo=new OrdersRepo():ordersRepo;
    }

    private OrdersRepo() {
    }

    public Single<Response<NewOrderResponse>> makeNewOrder(OrderPayload orderPayload){
        return BaseClient
                .getApiService()
                .makeNewOrder(orderPayload);

    }

    public MutableLiveData<CurrentOrderResponse> getCurrentOrder(String token) {
        currentOrder=new MutableLiveData<>();
        isCurrentOrderLoading.setValue(true);
        BaseClient.getApiService()
                .getNewCurrentOrder(token)
                .enqueue(new Callback<CurrentOrderResponse>() {
                    @Override
                    public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                        CurrentOrderResponse currentOrderResponse=response.body();
                        if (response.isSuccessful()&&currentOrderResponse!=null){
                            currentOrder.setValue(currentOrderResponse);
                        }else {
                            currentOrderLoadingError.setValue(Resources.getSystem().getString(R.string.server_error));
                        }
                        isCurrentOrderLoading.setValue(false);
                    }

                    @Override
                    public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
                        Crashlytics.logException(t);
                        Log.d("CONFIRMMMM", "onFailure: "+t.getMessage());
                        isCurrentOrderLoading.setValue(false);
                        //currentOrderLoadingError.setValue(Resources.getSystem().getString(R.string.server_error));
                        currentOrderLoadingError.setValue(App.getContext().getString(R.string.server_error));
                    }
                });
        return currentOrder;
    }

    public MutableLiveData<Boolean> getIsCurrentOrderLoading() {
        return isCurrentOrderLoading;
    }

    public MutableLiveData<String> getCurrentOrderLoadingError() {
        return currentOrderLoadingError;
    }

    // ----------------------- order update -----------------------------

    public MutableLiveData<UpdateOrderNewResponse> updateOrder(String token,
                                                               String order_id,
                                                                String user_id,
                                                               String status){
        updateOrder=new MutableLiveData<>();
        BaseClient
                .getApiService()
                .updateOrders(token,order_id,user_id,status)
                .enqueue(new Callback<UpdateOrderNewResponse>() {
                    @Override
                    public void onResponse(Call<UpdateOrderNewResponse> call, Response<UpdateOrderNewResponse> response) {
                        UpdateOrderNewResponse updateOrderNewResponse=response.body();
                        if (response.isSuccessful()&&updateOrderNewResponse!=null)
                            updateOrder.setValue(updateOrderNewResponse);
                        else
                            orderUpdateError.setValue(App.getContext().getString(R.string.server_error));

                        isOrderUpdating.setValue(true);
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {
                        Crashlytics.logException(t);
                        orderUpdateError.setValue(App.getContext().getString(R.string.server_error));

                    }
                });

        return updateOrder;
    }


    public MutableLiveData<Boolean> getIsOrderUpdating() {
        return isOrderUpdating;
    }

    public MutableLiveData<String> getOrderUpdateError() {
        return orderUpdateError;
    }
    
    //________________________________updateDelayedOrders_________________________________
    
    public Single<Response<UpdateOrderNewResponse>> updateDelayedOrders( String token,
                                                                         String order_id,
                                                                         String delayed_until,
                                                                         String user_id,
                                                                         String status)
    {
        return BaseClient
                .getApiService()
                .updateDelayedOrders2(token,order_id,delayed_until,user_id,status);
    }
}
