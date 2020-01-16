package com.greyeg.tajr.server;

import com.greyeg.tajr.models.ActivityHistory;
import com.greyeg.tajr.models.AddReasonResponse;
import com.greyeg.tajr.models.AdminRecordsResponse;
import com.greyeg.tajr.models.AllProducts;
import com.greyeg.tajr.models.BotBlocksResponse;
import com.greyeg.tajr.models.Broadcast;
import com.greyeg.tajr.models.CallTimePayload;
import com.greyeg.tajr.models.CallTimeResponse;
import com.greyeg.tajr.models.CancellationReasonsResponse;
import com.greyeg.tajr.models.CardsResponse;
import com.greyeg.tajr.models.CartResponse;
import com.greyeg.tajr.models.CashRequestHistory;
import com.greyeg.tajr.models.Cities;
import com.greyeg.tajr.models.DeleteAddProductResponse;
import com.greyeg.tajr.models.MoneyHistory;
import com.greyeg.tajr.models.MoneyRequestResponse;
import com.greyeg.tajr.models.NewOrderResponse;
import com.greyeg.tajr.models.OrderPayload;
import com.greyeg.tajr.models.OrderStatusHistoryResponse;
import com.greyeg.tajr.models.PointsHistory;
import com.greyeg.tajr.models.RemainingOrdersResponse;
import com.greyeg.tajr.models.MainResponse;
import com.greyeg.tajr.models.SimpleOrderResponse;
import com.greyeg.tajr.models.SimpleResponse;
import com.greyeg.tajr.models.SubscriberInfo;
import com.greyeg.tajr.models.ToalAvailableBalance;
import com.greyeg.tajr.models.UpdateOrderNewResponse;
import com.greyeg.tajr.models.UpdateOrderResponse;
import com.greyeg.tajr.models.UploadPhoneResponse;
import com.greyeg.tajr.models.UploadVoiceResponse;
import com.greyeg.tajr.models.UserOrders;
import com.greyeg.tajr.models.UserResponse;
import com.greyeg.tajr.models.UserTimePayload;
import com.greyeg.tajr.models.UserWorkTimeResponse;
import com.greyeg.tajr.order.models.CurrentOrderResponse;
import com.greyeg.tajr.order.models.SingleOrderProductsResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    // log in user client
    @FormUrlEncoded
    @POST("send/login")
    Call<UserResponse> login(@Field("username") String email, @Field("password") String password);

    // log in user client
    @FormUrlEncoded
    @POST("send/get_products")
    Call<AllProducts> getProducts(@Field("token") String token,
                                  @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("send/get_products")
    Single<Response<AllProducts>> getProducts2(@Field("token") String token,
                                                           @Field("user_id") String user_id,
                                               @Query("page") String page,
                                               @Query("limit") String limit);

    @FormUrlEncoded
    @POST("send/get_status_history")
    Call<OrderStatusHistoryResponse> getSatusHistoryResponse(@Field("token") String token,
                                                             @Field("order_id") String order_id,
                                                             @Field("status_type") String status_type,
                                                             @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("send/available_cards")
    Call<CardsResponse> getCards(@Field("token") String token);

    ///todo remove old call
    @FormUrlEncoded
    @POST("send/set_active_time")
    Call<UserWorkTimeResponse> userWorkTime(@Field("token") String token,
                                            @Field("activity") String activity,
                                            @Field("user_id") String user_id,
                                            @Field("action") String action);


    @FormUrlEncoded
    @POST("send/set_active_time")
    Single<Response<UserWorkTimeResponse>> sendWorkTime(@Field("token") String token,
                                                       @Field("activity") String activity,
                                                       @Field("user_id") String user_id,
                                                       @Field("action") String action);


    @FormUrlEncoded
    @POST("send/set_user_time")
    Single<Response<MainResponse>> set_user_time(UserTimePayload userTimePayload);

    @Multipart
    @POST("send/upload_voice_notes")
    Call<UploadVoiceResponse> uploadVoice(@Part("token") RequestBody token,
                                          @Part("order_id") RequestBody order_id,
                                          @Part("call_duration") RequestBody call_duration,
                                          @Part MultipartBody.Part audio);

    @Headers({"Content-Type: application/json"})
    @GET("send/get_orders")
    Call<CurrentOrderResponse> getNewCurrentOrder(@Query("token") String token);

    @FormUrlEncoded
    @POST("send/cpanel_login")
    Call<UserResponse> adminLogin(@Field("apiKey") String email, @Field("apiSecret") String password);

    @FormUrlEncoded
    @POST("send/logout")
    Call<UserResponse> logout(@Field("token") String token, @Field("user_id") String user_id);

    @Headers({"Content-Type: application/json"})
    @FormUrlEncoded
    @POST("send/fetch_orders")
    Call<UserOrders> getOrders(@Field("token") String token);


    // log in user client
    @FormUrlEncoded
    @POST("send/new_order")
    Call<NewOrderResponse> recordNewOrder(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id,
            @Field("client_name") String client_name,
            @Field("client_phone") String client_phone,
            @Field("city_id") String city_id,
            @Field("client_area") String client_area,
            @Field("client_address") String client_address,
            @Field("items") String items,
            @Field("discount") String discount,
            @Field("sender_name") String sender_name,
            @Field("sender_id") String sender_id
    );


    @POST("send/record_new_order")
    Single<Response<NewOrderResponse>> makeNewOrder(@Body OrderPayload orderPayload);

    @FormUrlEncoded
    @POST("send/update_order")
    Call<UpdateOrderNewResponse> updateOrders(
            @Field("token") String token,
            @Field("order_id") String order_id,
            @Field("user_id") String user_id,
            @Field("status") String status

    );

    // TODO clean it with updateClientData()
    @FormUrlEncoded
    @POST("send/set_client_data")
    Call<SimpleOrderResponse> updateOrderData(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("client_name") String client_name,
            @Field("client_address") String client_address,
            @Field("client_area") String client_area,
            @Field("notes") String notes

    );

    @FormUrlEncoded
    @POST("send/set_client_data")
    Call<CurrentOrderResponse> updateClientData(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("client_name") String client_name,
            @Field("client_address") String client_address,
            @Field("client_area") String client_area,
            @Field("notes") String notes

    );


    @FormUrlEncoded
    @POST("send/set_order_data")
    Call<CurrentOrderResponse> updateSingleOrderData(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("product_id") String product_id,
            @Field("client_city") String client_city,
            @Field("items_no") String items_no,
            @Field("discount") String discount

    );

    @FormUrlEncoded
    @POST("send/set_order_data")
    Call<CurrentOrderResponse> updateOrderMultiOrderData(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("client_city") String client_city,
            @Field("discount") String discount

    );

    @FormUrlEncoded
    @POST("send/remaining_orders")
    Call<RemainingOrdersResponse> getRemainingOrders(@Field("token") String token);

    @FormUrlEncoded
    @POST("send/add_product_to_order")
    Call<DeleteAddProductResponse> addProduct(
            @Field("token") String token,
            @Field("order_id") String order_id,
            @Field("product_id") String product_id,
            @Field("user_id") String user_id,
            @Field("items_no") String items_no
    );


    @FormUrlEncoded
    @POST("send/delete_product_from_order")
    Call<DeleteAddProductResponse> deleteProduct(
            @Field("token") String token,
            @Field("order_id") String order_id,
            @Field("extra_product_key") String extra_product_key,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id
    );

    @FormUrlEncoded
    @POST("send/shipping_attempts")
    Call<UpdateOrderNewResponse> updateShippingOrders(
            @Field("token") String token,
            @Field("order_id") String order_id,
            @Field("action") String action,
            @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("send/send_problem")
    Call<SimpleResponse> sendProblemForOrder(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("problem") String problem

    );


    @FormUrlEncoded
    @POST("send/phone")
    Call<UploadPhoneResponse> missedCall(@Field("token") String token,
                                         @Field("phone") String phone);

    @FormUrlEncoded
    @POST("send/update_order")
    Call<UpdateOrderNewResponse> updateDelayedOrders(
            @Field("token") String token,
            @Field("order_id") String order_id,
            @Field("delayed_until") String delayed_until,
            @Field("user_id") String user_id,
            @Field("status") String status

    );


    ///////////////////////////////////////////////////////


    // log in user client
    @FormUrlEncoded
    @POST("send/send_problem")
    Call<UpdateOrderResponse> sendProblem(
            @Field("token") String token,
            @Field("user_id") int user_id,
            @Field("order_id") int order_id,
            @Field("problem") String problem

    );

    @Headers({"Content-Type: application/json"})
    @GET("send/get_orders")
    Call<SimpleOrderResponse> getFuckenOrders(@Query("token") String token);


    @FormUrlEncoded
    @POST("send/update_order")
    Call<UpdateOrderNewResponse> updateOrders(
            @Field("token") String token,
            @Field("order_id") String order_id,
            @Field("status") String status

    );


    @FormUrlEncoded
    @POST("send/confirm_shipper_status")
    Call<UpdateOrderNewResponse> confirm_shipper_status(
            @Field("token") String token,
            @Field("order_id") String order_id

    );

    // log in user client
    @FormUrlEncoded
    @POST("send/phone_search")
    Call<SimpleOrderResponse> getPhoneData(@Field("token") String token,
                                           @Field("user_id") String user_id,
                                           @Field("phone") String phone);

    // log in user client
    @FormUrlEncoded
    @POST("send/phone_search")
    Call<CurrentOrderResponse> getPhoneData2(@Field("token") String token,
                                           @Field("user_id") String user_id,
                                           @Field("phone") String phone);




    @FormUrlEncoded
    @POST("send/set_order_data")
    Call<SimpleOrderResponse> updateOrderCalculationsSingleOrder(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("product_id") String product_id,
            @Field("client_city") String client_city,
            @Field("items_no") String items_no,
            @Field("discount") String discount

    );

    @FormUrlEncoded
    @POST("send/set_order_data")
    Call<SimpleOrderResponse> updateOrderCalculationsMultiOrder(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("client_city") String client_city,
            @Field("discount") String discount

    );


    // log in user client
    @FormUrlEncoded
    @POST("send/get_cities")
    Call<Cities> getCities(@Field("token") String token, @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("send/get_cities")
    Single<Response<Cities>> getCities2(@Field("token") String token, @Field("user_id") String user_id);

    // log in user client
    @FormUrlEncoded
    @POST("send/get_products")
    Call<SingleOrderProductsResponse> getSingleOrderProducts(@Field("token") String token,
                                                             @Field("user_id") String user_id);

    // log in user client
    @FormUrlEncoded
    @POST("send/activity_history")
    Call<ActivityHistory> getActivityHistory(@Field("token") String token,
                                             @Field("user_id") String user_id);

    // log in user client
    @FormUrlEncoded
    @POST("send/points_history")
    Call<PointsHistory> getPointsHistory(@Field("token") String token,
                                         @Field("user_id") String user_id);

    // log in user client
    @FormUrlEncoded
    @POST("send/cash_history")
    Call<MoneyHistory> getMoneyHistory(@Field("token") String token);


    // log in user client
    @FormUrlEncoded
    @POST("send/cash_out")
    Call<MoneyRequestResponse> requestCash(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("mobile") String mobile,
            @Field("cash") String cash,
            @Field("notes") String notes
    );

    // log in user client
    @FormUrlEncoded
    @POST("send/balance_history")
    Call<CashRequestHistory> getAvailableBalance(@Field("token") String token);

    // log in user client

    // log in user client
    @FormUrlEncoded
    @POST("send/retrieve_cards")
    Call<CartResponse> getCartDetails(@Field("token") String token, @Field("amount") String amount, @Field("type") String type);

    @FormUrlEncoded
    @POST("send/balance")
    Call<ToalAvailableBalance> getNormalAvailableBalance(@Field("token") String token, @Field("type") String type);

    @FormUrlEncoded
    @POST("send/balance")
    Call<ToalAvailableBalance> getCustomelAvailableBalance(@Field("token") String token,
                                                           @Field("type") String type,
                                                           @Field("year") String year,
                                                           @Field("month") String month,
                                                           @Field("day") String day
    );



    // log in user client
    @FormUrlEncoded
    @POST("send/all_records")
    Call<AdminRecordsResponse> getRecords(@Field("token") String token);

    @FormUrlEncoded
    @POST("send/get_subscriber_info")
    Single<Response<SubscriberInfo>> getSubscriberInfo(@Field("token") String token,@Field("sender_name") String sender_name);

    @FormUrlEncoded
    @POST("send/get_bot_blocks")
    Single<Response<BotBlocksResponse>> getBotBlocks(@Field("token") String token);

    @FormUrlEncoded
    @POST("send/broadcast")
    Single<Response<Broadcast>> sendBroadcast(@Field("token") String token, @Field("subscriber") String subscriber
            , @Field("page") String page, @Field("block") String block);

    @FormUrlEncoded
    @POST("send/cancellation_reasons")
    Call<CancellationReasonsResponse> getCancellationReasons(@Field("token") String token);

    @FormUrlEncoded
    @POST("send/add_cancellation_reason")
    Call<AddReasonResponse> submitNewCancellationReason(@Field("token") String token,@Field("name") String name);


    @FormUrlEncoded
    @POST("send/add_reason_to_order")
    Call<MainResponse> addReasonToOrder(@Field("token") String token,
                                        @Field("order_id") String order_id,
                                        @Field("reason_id") String reason_id);


    @POST("send/set_call_time")
    Single<Response<CallTimeResponse>> setCallTime(@Body CallTimePayload callTImePayload);

}
