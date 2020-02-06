package com.greyeg.tajr.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.greyeg.tajr.R;
import com.greyeg.tajr.models.AllProducts;
import com.greyeg.tajr.models.CardsResponse;
import com.greyeg.tajr.models.Cities;
import com.greyeg.tajr.models.DeleteAddProductResponse;
import com.greyeg.tajr.models.NewOrderResponse;
import com.greyeg.tajr.models.OrderStatusHistoryResponse;
import com.greyeg.tajr.models.RemainingOrdersResponse;
import com.greyeg.tajr.models.SimpleResponse;
import com.greyeg.tajr.models.UpdateOrderNewResponse;
import com.greyeg.tajr.models.UploadPhoneResponse;
import com.greyeg.tajr.models.UploadVoiceResponse;
import com.greyeg.tajr.models.UserWorkTimeResponse;
import com.greyeg.tajr.models.CurrentOrderResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    @BindView(R.id.error)
    TextView error;

    @BindView(R.id.success)
    TextView success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        chooseDate();

    }

    public void getCitiesTest() {

        BaseClient.getBaseClient().create(Api.class).getCities("YIXRKEsDUv4VpAP5BaroqlJb", "108")
                .enqueue(new Callback<Cities>() {
                    @Override
                    public void onResponse(Call<Cities> call, Response<Cities> response) {
                        System.out.println(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<Cities> call, Throwable t) {

                        System.out.println(t.getMessage());
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public void getProducts() {
        BaseClient.getBaseClient().create(Api.class).getProducts("YIXRKEsDUv4VpAP5BaroqlJb", "108")
                .enqueue(new Callback<AllProducts>() {
                    @Override
                    public void onResponse(Call<AllProducts> call, Response<AllProducts> response) {
                        System.out.println(response.body().getCode());
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<AllProducts> call, Throwable t) {
                        System.out.println(t.getMessage());
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public void getStatusHistoryTest() {

        BaseClient.getBaseClient().create(Api.class)
                .getSatusHistoryResponse("YIXRKEsDUv4VpAP5BaroqlJb", "8297", "order", "108")
                .enqueue(new Callback<OrderStatusHistoryResponse>() {
                    @Override
                    public void onResponse(Call<OrderStatusHistoryResponse> call, Response<OrderStatusHistoryResponse> response) {
                        System.out.println(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<OrderStatusHistoryResponse> call, Throwable t) {
                        System.out.println(t.getMessage());
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public void availableCardsTest() {
        BaseClient.getBaseClient().create(Api.class).getCards("YIXRKEsDUv4VpAP5BaroqlJb")
                .enqueue(new Callback<CardsResponse>() {
                    @Override
                    public void onResponse(Call<CardsResponse> call, Response<CardsResponse> response) {
                        System.out.println(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<CardsResponse> call, Throwable t) {
                        System.out.println(t.getMessage());
                        Log.d(TAG, "onResponse: " + t.getMessage());

                    }
                });
    }

    void getSatusHistoryResponse() {
        BaseClient.getBaseClient().create(Api.class)
                .getSatusHistoryResponse("YIXRKEsDUv4VpAP5BaroqlJb", "8297", "order", "108")
                .enqueue(new Callback<OrderStatusHistoryResponse>() {
                    @Override
                    public void onResponse(Call<OrderStatusHistoryResponse> call, Response<OrderStatusHistoryResponse> response) {
                        System.out.println(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<OrderStatusHistoryResponse> call, Throwable t) {
                        System.out.println(t.getMessage());
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public void setActivrTime() {
        BaseClient.getBaseClient().create(Api.class).userWorkTime("YIXRKEsDUv4VpAP5BaroqlJb", "55", "108","APP")
                .enqueue(new Callback<UserWorkTimeResponse>() {
                    @Override
                    public void onResponse(Call<UserWorkTimeResponse> call, Response<UserWorkTimeResponse> response) {
                        System.out.println(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<UserWorkTimeResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        System.out.println(t.getMessage());
                    }
                });
    }

    private void uploadVoices() {

        String path = Environment.getExternalStorageDirectory() + "/MyRecords/28_4_2019/01013014146_1:49:16 PM.mp4";
        File file = new File(path);
        RequestBody surveyBody = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Part record = MultipartBody.Part.createFormData("voice_note", file.getName(), surveyBody);
        RequestBody title1 = RequestBody.create(MediaType.parse("text/plain"), "8297");
        RequestBody duration = RequestBody.create(MediaType.parse("text/plain"), "40");
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), "YIXRKEsDUv4VpAP5BaroqlJb");

        BaseClient.getBaseClient().create(Api.class).uploadVoice(token, title1, duration, record).enqueue(new Callback<UploadVoiceResponse>() {
            @Override
            public void onResponse(Call<UploadVoiceResponse> call2, Response<UploadVoiceResponse> response) {

                success.setText(response.body().toString());
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<UploadVoiceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                error.setText(t.getMessage());

            }
        });

    }

    public void getCurrentOrder() {

        BaseClient.getBaseClient().create(Api.class).getNewCurrentOrder("pvBZJQ6tEeWDO8UjTnxdcboP")
                .enqueue(new Callback<CurrentOrderResponse>() {
                    @Override
                    public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                        success.setText(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.toString());
                    }

                    @Override
                    public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        error.setText(t.getMessage());
                    }
                });

    }

    public void newOrder() {
        //todo add pssid and sender name to order
        BaseClient.getBaseClient().create(Api.class).recordNewOrder("pvBZJQ6tEeWDO8UjTnxdcboP",
                "127",
                "357", "eslam", "01067457655"
                , "113", "sandbis", "amaira"
                , "2", "0",null,null)
                .enqueue(new Callback<NewOrderResponse>() {
                    @Override
                    public void onResponse(Call<NewOrderResponse> call, Response<NewOrderResponse> response) {
                        success.setText(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.toString());
                    }

                    @Override
                    public void onFailure(Call<NewOrderResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        error.setText(t.getMessage());
                    }
                });
    }

    public void updateOrder() {
        BaseClient.getBaseClient().create(Api.class).updateOrders("pvBZJQ6tEeWDO8UjTnxdcboP", "8329",
                "127", "client_noanswer")
                .enqueue(new Callback<UpdateOrderNewResponse>() {
                    @Override
                    public void onResponse(Call<UpdateOrderNewResponse> call, Response<UpdateOrderNewResponse> response) {
                        success.setText(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.toString());
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {

                        Log.d(TAG, "onFailure: " + t.getMessage());
                        error.setText(t.getMessage());
                    }
                });
    }

    public void updateClientData() {
        BaseClient.getBaseClient().create(Api.class).updateClientData(
                "pvBZJQ6tEeWDO8UjTnxdcboP",
                "127", "8329",
                "client_noanswer",
                "", "", ""
        ).enqueue(new Callback<CurrentOrderResponse>() {
            @Override
            public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                success.setText(response.body().toString());
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                error.setText(t.getMessage());
            }
        });
    }

    public void updateSingleOrderData() {
        BaseClient.getBaseClient().create(Api.class).updateSingleOrderData(
                "pvBZJQ6tEeWDO8UjTnxdcboP",
                "127", "8329",
                "370",
                "118", "1", "0"
        ).enqueue(new Callback<CurrentOrderResponse>() {
            @Override
            public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                success.setText(response.body().toString());
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                error.setText(t.getMessage());
            }
        });
    }

    public void updateOrderMultiOrderData() {
        BaseClient.getBaseClient().create(Api.class).updateOrderMultiOrderData(
                "pvBZJQ6tEeWDO8UjTnxdcboP",
                "127", "8329",
                "370",
                "118"
        ).enqueue(new Callback<CurrentOrderResponse>() {
            @Override
            public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                success.setText(response.body().toString());
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                error.setText(t.getMessage());
            }
        });
    }

    public void updtaeOrderShippingAttempts() {
        BaseClient.getBaseClient().create(Api.class).updateShippingOrders(
                "pvBZJQ6tEeWDO8UjTnxdcboP",
                "7550", "client_noanswer",
                "127"
        ).enqueue(new Callback<UpdateOrderNewResponse>() {
            @Override
            public void onResponse(Call<UpdateOrderNewResponse> call, Response<UpdateOrderNewResponse> response) {
                success.setText(response.body().toString());
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                error.setText(t.getMessage());
            }
        });
    }

    public void getRemainingOrders() {
        BaseClient.getBaseClient().create(Api.class).getRemainingOrders(
                "pvBZJQ6tEeWDO8UjTnxdcboP").enqueue(new Callback<RemainingOrdersResponse>() {
            @Override
            public void onResponse(Call<RemainingOrdersResponse> call, Response<RemainingOrdersResponse> response) {
                success.setText(response.body().toString());
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<RemainingOrdersResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                error.setText(t.getMessage());
            }
        });
    }

    public void addProduactToOrder() {
        BaseClient.getBaseClient().create(Api.class).addProduct(
                "pvBZJQ6tEeWDO8UjTnxdcboP",
                "8320",
                "370",
                "127",
                "1"
        ).enqueue(new Callback<DeleteAddProductResponse>() {
            @Override
            public void onResponse(Call<DeleteAddProductResponse> call, Response<DeleteAddProductResponse> response) {
                success.setText(response.body().toString());
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<DeleteAddProductResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                error.setText(t.getMessage());
            }
        });
    }

    public void deleteProducte() {
        BaseClient.getBaseClient().create(Api.class).deleteProduct(
                "pvBZJQ6tEeWDO8UjTnxdcboP",
                "8320",
                "2", "127", "8320"
        ).enqueue(new Callback<DeleteAddProductResponse>() {
            @Override
            public void onResponse(Call<DeleteAddProductResponse> call, Response<DeleteAddProductResponse> response) {
                success.setText(response.body().toString());
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<DeleteAddProductResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                error.setText(t.getMessage());
            }
        });
    }

    public void sendProblemForOrder() {
        BaseClient.getBaseClient().create(Api.class).sendProblemForOrder(
                "pvBZJQ6tEeWDO8UjTnxdcboP",
                "127",
                "8320",
                "127"
        ).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                success.setText(response.body().toString());
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                error.setText(t.getMessage());
            }
        });
    }

    public void missedCall() {
        BaseClient.getBaseClient().create(Api.class).missedCall("pvBZJQ6tEeWDO8UjTnxdcboP",
                "01067457665")
                .enqueue(new Callback<UploadPhoneResponse>() {
                    @Override
                    public void onResponse(Call<UploadPhoneResponse> call, Response<UploadPhoneResponse> response) {
                        success.setText(response.body().toString());
                        Log.d(TAG, "onResponse: " + response.toString());

                    }

                    @Override
                    public void onFailure(Call<UploadPhoneResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        error.setText(t.getMessage());
                    }
                });
    }

    private void delayOrder(){

    }


    private void chooseDate() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker =
                new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    calendar.set(year1, month1, dayOfMonth);
                    String dateString = sdf.format(calendar.getTime());

                    Api api = BaseClient.getBaseClient().create(Api.class);
                    api.updateDelayedOrders(
                            "pvBZJQ6tEeWDO8UjTnxdcboP",
                            "8320",
                            dateString,
                            "127",
                            "client_delay"
                    ).enqueue(new Callback<UpdateOrderNewResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<UpdateOrderNewResponse> call, @NotNull Response<UpdateOrderNewResponse> response) {
                            success.setText(response.body().toString());
                            Log.d(TAG, "onResponse: " + response.toString());

                        }

                        @Override
                        public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                            error.setText(t.getMessage());
                        }
                    });
                }, year, month, day); // set date picker to current date

        datePicker.show();

        datePicker.setOnCancelListener(dialog -> dialog.dismiss());
    }
}
