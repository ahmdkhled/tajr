package com.greyeg.tajr.order.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.adapters.OrderProductsAdapter;
import com.greyeg.tajr.helper.CallTimeManager;
import com.greyeg.tajr.helper.NetworkUtil;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.ViewAnimation;
import com.greyeg.tajr.models.CallActivity;
import com.greyeg.tajr.models.CallTimePayload;
import com.greyeg.tajr.models.CallTimeResponse;
import com.greyeg.tajr.models.DeleteAddProductResponse;
import com.greyeg.tajr.models.ExtraData;
import com.greyeg.tajr.models.MainResponse;
import com.greyeg.tajr.models.OrderProduct;
import com.greyeg.tajr.models.RemainingOrdersResponse;
import com.greyeg.tajr.models.UpdateOrderNewResponse;
import com.greyeg.tajr.order.CurrentOrderData;
import com.greyeg.tajr.order.NewOrderActivity;
import com.greyeg.tajr.order.adapters.MultiOrderProductsAdapter;
import com.greyeg.tajr.order.adapters.SignleOrderProductsAdapter;
import com.greyeg.tajr.order.enums.OrderProductsType;
import com.greyeg.tajr.order.enums.OrderUpdateStatusEnums;
import com.greyeg.tajr.order.enums.ResponseCodeEnums;
import com.greyeg.tajr.order.models.City;
import com.greyeg.tajr.order.models.CurrentOrderResponse;
import com.greyeg.tajr.order.models.MultiOrderProducts;
import com.greyeg.tajr.order.models.Order;
import com.greyeg.tajr.order.models.Product;
import com.greyeg.tajr.order.models.SingleOrderProductsResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.sheets.FragmentBottomSheetDialogFull;
import com.greyeg.tajr.view.dialogs.AddProductDialog;
import com.greyeg.tajr.view.dialogs.CancelOrderDialog;
import com.greyeg.tajr.view.dialogs.Dialogs;
import com.greyeg.tajr.view.dialogs.ProductDetailDialog;
import com.greyeg.tajr.viewmodels.CurrentOrderViewModel;
import com.tapadoo.alerter.Alerter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.greyeg.tajr.activities.LoginActivity.IS_LOGIN;

public class CurrentOrderFragment extends Fragment
        implements CancelOrderDialog.OnReasonSubmitted
        ,OrderProductsAdapter.OnProductItemEvent
        ,ProductDetailDialog.OnProductUpdated
        ,AddProductDialog.OnProductAdded{

    private final String TAG = "CurrentOrderFragment";
    @BindView(R.id.client_name)
    EditText client_name;
    @BindView(R.id.client_address)
    EditText client_address;
    @BindView(R.id.client_area)
    EditText client_area;
    @BindView(R.id.client_order_phone1)
    EditText client_order_phone1;
    @BindView(R.id.status)
    EditText status;
    @BindView(R.id.shipping_status)
    EditText shipping_status;
    @BindView(R.id.shipping_cost)
    EditText shipping_cost;
    @BindView(R.id.sender_name)
    EditText sender_name;
    @BindView(R.id.ntes)
    EditText notes;
    @BindView(R.id.discount)
    EditText discount;
    @BindView(R.id.order_total_cost)
    EditText order_total_cost;
    @BindView(R.id.client_feedback)
    EditText client_feedback;
    @BindView(R.id.order_id)
    EditText order_id;
    @BindView(R.id.client_city)
    Spinner client_city;
    @BindView(R.id.add_product)
    ImageView add_product;
    @BindView(R.id.order_type)
    EditText order_type;
    @BindView(R.id.ProgressBar)
    ProgressBar mProgressBar4;
    @BindView(R.id.present)
    TextView present;


    @BindView(R.id.back_drop)
    View back_drop;
    // update normal order
    @BindView(R.id.normal_order_data_confirmed)
    CardView normal_order_data_confirmed;
    @BindView(R.id.normal_client_phone_error)
    CardView normal_client_phone_error;
    @BindView(R.id.normal_no_answer)
    CardView normal_no_answer;
    @BindView(R.id.normal_delay)
    CardView normal_delay;
    @BindView(R.id.normal_client_cancel)
    CardView normal_client_cancel;
    @BindView(R.id.normal_busy)
    CardView normal_busy;

    @BindView(R.id.normalUpdateButton)
    FloatingActionButton normalUpdateButton;

    @BindView(R.id.normalUpdateButtonShipping)
    FloatingActionButton normalUpdateButtonShipping;

    @BindView(R.id.normal_update_actions)
    View normal_update_actions;

    @BindView(R.id.shipper_update_actions)
    View shipper_update_actions;



    @BindView(R.id.deliver)
    CardView deliver;
    @BindView(R.id.shipping_no_answer)
    CardView shipping_no_answer;
    @BindView(R.id.return_order)
    CardView return_order;
    @BindView(R.id.productsRecycler)
    RecyclerView productsRecycler;

    // main view of the CurrentOrderFragment
    private View mainView;
    private boolean firstOrder;
    private int firstRemaining;
    private Dialog errorGetCurrentOrderDialog;
    private ProgressDialog progressDialog;

    private boolean rotate = false;
    private boolean rotateshipper = false;
    private boolean productExbandable = false;
    private CurrentOrderViewModel currentOrderViewModel;
    private CancelOrderDialog cancelOrderDialog;
    private CancelOrderDialog.OnReasonSubmitted onReasonSubmitted=this;
    private long orderId=-1;
    private OrderProductsAdapter orderProductsAdapter;
    private AddProductDialog.OnProductAdded onProductAdded;
    private AddProductDialog addProductDialog;

    public CurrentOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_current_order, container, false);
        ButterKnife.bind(this, mainView);


        onProductAdded= this;
        initLabels();
        setListeners();
        currentOrderViewModel= ViewModelProviders.of(getActivity()).get(CurrentOrderViewModel.class);

        Bundle bundle = getArguments();
        if (bundle!=null){
            if (bundle.getString("fromBuble")!=null){
                if (bundle.getString("fromBuble").equals("buble")){
                    getBubleOrder();
                }else getCurrentOrder();
            }else getCurrentOrder();
        }else getCurrentOrder();

        return mainView;
    }



    private void setListeners() {
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addProductToMultiOrdersTv();
                addProductDialog=new AddProductDialog(onProductAdded);
                if (getFragmentManager() != null) {
                    addProductDialog.show(getFragmentManager(),"");
                }
            }
        });
        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_drop.setVisibility(View.GONE);
                toggleFabMode(normalUpdateButton);
            }
        });

        normalUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(normalUpdateButton);
            }
        });
        normalUpdateButtonShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabModeShipper(normalUpdateButtonShipping);
            }
        });

        normal_busy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(normalUpdateButton);
                normalUpdateOrder(OrderUpdateStatusEnums.client_busy.name());
            }
        });

        normal_client_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(normalUpdateButton);
                cancelOrderDialog=new CancelOrderDialog(onReasonSubmitted);
                cancelOrderDialog.show(getChildFragmentManager(),"CANCEL");
                //normalUpdateOrder(OrderUpdateStatusEnums.client_cancel.name());
            }
        });

        normal_client_phone_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(normalUpdateButton);
                normalUpdateOrder(OrderUpdateStatusEnums.client_phone_error.name());
            }
        });

        normal_no_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(normalUpdateButton);
                normalUpdateOrder(OrderUpdateStatusEnums.client_noanswer.name());
            }
        });

        normal_order_data_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(normalUpdateButton);

                updateClientData();
                //normalUpdateOrder(OrderUpdateStatusEnums.order_data_confirmed.name());

//                Map<String,Object> values =getExtraDataValues();
//                Log.d("VALUEESSS", "onClick: "+values.toString());
            }
        });

        normal_delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(normalUpdateButton);
                chooseDate();
            }
        });


        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabModeShipper(normalUpdateButtonShipping);
                updateShippingOrder("deliver");
            }
        });

        return_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabModeShipper(normalUpdateButtonShipping);
                updateShippingOrder("return");
            }
        });


        shipping_no_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabModeShipper(normalUpdateButtonShipping);
                updateShippingOrder("client_noanswer");
            }
        });

    }

    private void observeAddingReasonToOrder(){
        currentOrderViewModel
                .addReasonToOrder()
        .observe(getActivity(), new Observer<MainResponse>() {
            @Override
            public void onChanged(MainResponse mainResponse) {
                Toast.makeText(getContext(), mainResponse.getData(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void observeIsReasonAddingToOrder(){
        currentOrderViewModel
                .getIsReasonAddingTOOrder()
                .observe(getActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        Log.d("REASONORDER", "onChanged: "+aBoolean);
                    }
                });
    }
    private void observeAddingReasonToOrderError(){
        currentOrderViewModel
                .getReasonAddingToOrderError()
                .observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(getContext(), R.string.adding_reason_to_order_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateShippingOrder(String action) {
        ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));
        Api api = BaseClient.getBaseClient().create(Api.class);
        api.updateShippingOrders(
                SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                action,
                CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId()
        ).enqueue(new Callback<UpdateOrderNewResponse>() {
            @Override
            public void onResponse(@NotNull Call<UpdateOrderNewResponse> call, @NotNull Response<UpdateOrderNewResponse> response) {
                progressDialog.dismiss();
                Log.d("CONFIRMMMM", "updateShippingOrder: ");
                getCurrentOrder();
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("CONFIRMMMM", "updateShippingOrder: failure");
                getCurrentOrder();
                Log.d(TAG, "onResponse: " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.bt_expand)
    void showCurrentProductDetails() {
        if (!productExbandable)
            return;
        FragmentBottomSheetDialogFull fragment = new FragmentBottomSheetDialogFull();
        fragment.show(getChildFragmentManager(), fragment.getTag());
    }

    public void updateClientData() {
        ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));
        BaseClient.getBaseClient().create(Api.class).updateClientData(
                SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                client_name.getText().toString(),
                client_address.getText().toString(),
                client_area.getText().toString(),
                notes.getText().toString()
        ).enqueue(new Callback<CurrentOrderResponse>() {
            @Override
            public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                if (CurrentOrderData.getInstance().getCurrentOrderResponse()
                        .getOrder().getOrderType().equals(OrderProductsType.SingleOrder.getType())) {
                    updateSingleOrderData(progressDialog);
                } else {
                    updateOrderMultiOrderData(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                showErrorGetCurrentOrderDialog(t.getMessage());
            }
        });
    }

    public void updateSingleOrderData(ProgressDialog progressDialog) {
        BaseClient.getBaseClient().create(Api.class).updateSingleOrderData(
                SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                orderProductsAdapter.getProducts().get(0).getId(),
                client_city.getTag().toString(),
                String.valueOf(orderProductsAdapter.getProducts().get(0).getItems_no()),
                discount.getText().toString().trim()
        ).enqueue(new Callback<CurrentOrderResponse>() {
            @Override
            public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                progressDialog.dismiss();
                Log.d("CONFIRMMMM", "updateSingleOrderData: ");
                normalUpdateOrder(OrderUpdateStatusEnums.order_data_confirmed.name());

                //getCurrentOrder();
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                showErrorGetCurrentOrderDialog(t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // display first sheet

    }

    public void updateOrderMultiOrderData(ProgressDialog progressDialog) {
        BaseClient.getBaseClient().create(Api.class).updateOrderMultiOrderData(
                SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                client_city.getTag().toString(),
                discount.getText().toString().trim()
        ).enqueue(new Callback<CurrentOrderResponse>() {
            @Override
            public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                progressDialog.dismiss();
                normalUpdateOrder(OrderUpdateStatusEnums.order_data_confirmed.name());

                //getCurrentOrder();
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                showErrorGetCurrentOrderDialog(t.getMessage());

            }
        });
    }

    private void normalUpdateOrder(String status) {
        String token=SharedHelper.getKey(getActivity(), LoginActivity.TOKEN);
        currentOrderViewModel
                .updateOrder(token,
                        CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                        CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                        status)
                .observe(getActivity(), new Observer<UpdateOrderNewResponse>() {
                    @Override
                    public void onChanged(UpdateOrderNewResponse updateOrderNewResponse) {
                        if (updateOrderNewResponse!=null){
                            Log.d("CONFIRMMMM", "onChanged: "+updateOrderNewResponse.getData());
                            handleCallTime(updateOrderNewResponse.getOrder_id(),updateOrderNewResponse.getHistory_line());
                            getCurrentOrder();
                        }else {
                            Log.d("CONFIRMMMM", "error: --------------");

                        }
                    }
                });
        observeOrderUpdating();
        observeOrderUpdatingError();

//
//        BaseClient.getBaseClient().create(Api.class).updateOrders(
//                SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
//                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
//                CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
//                status
//        )
//                .enqueue(new Callback<UpdateOrderNewResponse>() {
//                    @Override
//                    public void onResponse(Call<UpdateOrderNewResponse> call, Response<UpdateOrderNewResponse> response) {
////                        progressDialog.dismiss();
////
////                        UpdateOrderNewResponse updateOrderNewResponse =response.body();
////                        if (updateOrderNewResponse!=null)
////                        handleCallTime(updateOrderNewResponse.getOrder_id(),updateOrderNewResponse.getHistory_line());
////                        getCurrentOrder();
////                        Log.d(TAG, "onResponse: " + response.toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {
//                        progressDialog.dismiss();
//                        Log.d(TAG, "onFailure: " + t.getMessage());
//                        showErrorGetCurrentOrderDialog(getString(R.string.error_has_occured));
//                    }
//                });
    }

    public void observeOrderUpdating(){
        currentOrderViewModel
                .getIsOrderUpdating()
                .observe(getActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        Log.d("DDIAALOOOOGGG", "order update: "+aBoolean);

                        if (aBoolean!=null&&aBoolean)
                            progressDialog=showProgressDialog(getActivity(), getString(R.string.fetching_th_order));
                        else
                            progressDialog.dismiss();

                    }
                });
    }

    public void observeOrderUpdatingError(){
        currentOrderViewModel
                .getOrderUpdatingError()
                .observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleCallTime(String order_id,String history_line){
        ArrayList<CallActivity> callActivity=CallTimeManager.getInstance(getContext())
                .getCallActivity();

        if (callActivity==null||callActivity.isEmpty()){
            Log.d("handleCallTime", "empty:" );
            return;
        }

        Log.d("handleCallTime", "history_line "+history_line );
        Log.d("handleCallTime", "order_id "+order_id );
        for (CallActivity activity:callActivity) {
            activity.setHistory_line(history_line);
            Log.d("handleCallTime", activity.getDuration() );

        }


        String token=SharedHelper.getKey(getContext(),LoginActivity.TOKEN);
        CallTimePayload payload=new CallTimePayload(token,order_id,callActivity);

        currentOrderViewModel
                .setCallTime(payload)
                .observe(getActivity(), new Observer<CallTimeResponse>() {
                    @Override
                    public void onChanged(CallTimeResponse callTimeResponse) {
                        Toast.makeText(getContext(), callTimeResponse.getData(), Toast.LENGTH_LONG).show();
                        CallTimeManager.getInstance(getContext())
                                .emptyCallsHistory();
                    }
                });

    }


    private void chooseDate() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker =
                new DatePickerDialog(getActivity(), (view, year1, month1, dayOfMonth) -> {

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    calendar.set(year1, month1, dayOfMonth);
                    String dateString = sdf.format(calendar.getTime());
                    ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));

                    Api api = BaseClient.getBaseClient().create(Api.class);
                    api.updateDelayedOrders(
                            SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                            CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                            dateString,
                            CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                            OrderUpdateStatusEnums.client_delay.name()
                    ).enqueue(new Callback<UpdateOrderNewResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<UpdateOrderNewResponse> call, @NotNull Response<UpdateOrderNewResponse> response) {
                            progressDialog.dismiss();
                            getCurrentOrder();
                            Log.d(TAG, "onResponse: " + response.toString());
                        }

                        @Override
                        public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.d(TAG, "onFailure: " + t.getMessage());
                            showErrorGetCurrentOrderDialog(t.getMessage());
                        }
                    });
                }, year, month, day); // set date picker to current date

        datePicker.show();

        datePicker.setOnCancelListener(dialog -> dialog.dismiss());
    }

    private void addProductToMultiOrdersTv() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_add_poduct_dialog);

        //Spinner productSpinner = dialog.findViewById(R.id.product_spinner);
        //productSpinner.setTag(OrderProductsType.MuhltiOrder.getType());
        EditText productNo = dialog.findViewById(R.id.product_no);
        TextView addProductBtn = dialog.findViewById(R.id.add_product);
        productNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int no;
                try {
                    no=Integer.valueOf(productNo.getText().toString());
                }catch (Exception e){
                    no=0;
                }
                if (no<1){
                    Toast.makeText(getContext(), R.string.enter_valid_quantity, Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                //addProductToMultiOrder(no, productSpinner.getSelectedItemPosition());
            }
        });
        dialog.show();

    }

    private void addProductToMultiOrder(int number, int index) {
        ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.add_product));
        BaseClient.getBaseClient().create(Api.class).addProduct(
                SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                CurrentOrderData.getInstance().getSingleOrderProductsResponse().getProducts().get(index).getProductId(),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                String.valueOf(number)
        ).enqueue(new Callback<DeleteAddProductResponse>() {
            @Override
            public void onResponse(Call<DeleteAddProductResponse> call, Response<DeleteAddProductResponse> response) {
                progressDialog.dismiss();
                if (response.body() != null) {

                    if (response.body().getCode().equals(ResponseCodeEnums.code_1200.getCode())) {
                        Toast.makeText(getActivity(), getString(R.string.added_success), Toast.LENGTH_SHORT).show();
                        getCurrentOrder();
                    }

                } else {
                    errorGetCurrentOrderDialog = Dialogs.showCustomDialog(getActivity(),
                            response.toString(), getString(R.string.order),
                            getString(R.string.retry), getString(R.string.finish_work), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    errorGetCurrentOrderDialog.dismiss();
                                    getCurrentOrder();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    NewOrderActivity.finishWork();
                                }
                            });
                }

            }

            @Override
            public void onFailure(Call<DeleteAddProductResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("DeleteAddProduct", "onFailure: " + t.getMessage());
                errorGetCurrentOrderDialog = Dialogs.showCustomDialog(getActivity(),
                        t.getMessage(), getString(R.string.order),
                        getString(R.string.retry), getString(R.string.finish_work), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorGetCurrentOrderDialog.dismiss();
                                getCurrentOrder();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NewOrderActivity.finishWork();
                            }
                        });
            }
        });

    }

    private void getBubleOrder() {
        CurrentOrderData.getInstance().setCurrentOrderResponse(CurrentOrderData.getInstance().getMissedCallOrderResponse());

        try {
            orderId=Long.valueOf(CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId());
            if (CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getCheckType().equals("normal_order")) {
                fillFieldsWithOrderData(CurrentOrderData.getInstance().getMissedCallOrderResponse());
                updateProgress();
                productExbandable = true;
                normal_update_actions.setVisibility(View.VISIBLE);
                shipper_update_actions.setVisibility(View.GONE);
            } else {
                fillFieldsWithOrderData(CurrentOrderData.getInstance().getMissedCallOrderResponse());
                updateProgress();
                productExbandable = true;
                normal_update_actions.setVisibility(View.GONE);
                shipper_update_actions.setVisibility(View.VISIBLE);

            }
        } catch (Exception e) {
            Log.e("eslamfaissal", "onResponse: ", e);
            Log.d("eslamfaissal", "onResponse: " + CurrentOrderData.getInstance().getMissedCallOrderResponse().toString());
            CurrentOrderData.getInstance().setCurrentOrderResponse(CurrentOrderData.getInstance().getMissedCallOrderResponse());
            fillFieldsWithOrderData(CurrentOrderData.getInstance().getMissedCallOrderResponse());
            updateProgress();
            productExbandable = true;
            normal_update_actions.setVisibility(View.VISIBLE);
            shipper_update_actions.setVisibility(View.GONE);
        }


    }

    private void getCurrentOrder(){

        if(!NetworkUtil.isConnected(getContext())){
            Alerter.create(getActivity())
                    .enableSwipeToDismiss()
                    .setBackgroundResource(R.color.red)
                    .setDuration(2500)
                    .setText(getString(R.string.no_connection_message))
                    .show();
            return;
        }
        Log.d("CONFIRMMMM", " getting CurrentOrder");

        currentOrderViewModel
                .getCurrentOrder(SharedHelper.getKey(getContext(),LoginActivity.TOKEN))
                .observe(getActivity(), new Observer<CurrentOrderResponse>() {
                    @Override
                    public void onChanged(CurrentOrderResponse currentOrderResponse) {
                        Log.d("CONFIRMMMM", " onChanged: getCurrentOrder ");
                        if (currentOrderResponse!=null){
                            if (currentOrderResponse.getCode().equals(ResponseCodeEnums.code_1200.getCode())) {
                                orderId= Long.valueOf(currentOrderResponse.getOrder().getId());
                                CurrentOrderData.getInstance().setCurrentOrderResponse(currentOrderResponse);

                                try {
                                    if (CurrentOrderData.getInstance().getCurrentOrderResponse()
                                            .getOrder().getCheckType().equals("normal_order")) {
                                        fillFieldsWithOrderData(currentOrderResponse);
                                        updateProgress();
                                        productExbandable = true;
                                        normal_update_actions.setVisibility(View.VISIBLE);
                                        shipper_update_actions.setVisibility(View.GONE);
                                    } else {
                                        fillFieldsWithOrderData(currentOrderResponse);
                                        updateProgress();
                                        productExbandable = true;
                                        normal_update_actions.setVisibility(View.GONE);
                                        shipper_update_actions.setVisibility(View.VISIBLE);

                                    }
                                } catch (Exception e) {
                                    Log.e("eslamfaissal", "onResponse: ", e);
                                    Log.d("eslamfaissal", "onResponse: " + currentOrderResponse.toString());
                                    CurrentOrderData.getInstance().setCurrentOrderResponse(currentOrderResponse);
                                    fillFieldsWithOrderData(currentOrderResponse);
                                    updateProgress();
                                    productExbandable = true;
                                    normal_update_actions.setVisibility(View.VISIBLE);
                                    shipper_update_actions.setVisibility(View.GONE);
                                }


                            }
                            else if (currentOrderResponse.getCode().equals(ResponseCodeEnums.code_1300.getCode())) {
                                // no new orders all handled
                                Dialogs.showCustomDialog(getActivity(), getString(R.string.no_more_orders), getString(R.string.order),
                                        "Back", null, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                getActivity().finish();
                                            }
                                        }, null);
                            } else if (ResponseCodeEnums.loginIssue(currentOrderResponse.getCode())) {

                                SharedHelper.putKey(getActivity(), IS_LOGIN, "no");
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        }else {
                            showErrorGetCurrentOrderDialog(getString(R.string.server_error));
                            Log.d(TAG, "onResponse: null = failure" );
                        }
                    }
                });
        observeLoadingCurrentOrder();
        observeLoadingCurrentOrderError();
    }

    private void observeLoadingCurrentOrder(){
        currentOrderViewModel
                .getIsCurrentOrderLoading()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        Log.d("DDIAALOOOOGGG", "onChanged: "+aBoolean);
                        if (aBoolean!=null&&aBoolean){
                             progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));

                        }else {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void observeLoadingCurrentOrderError(){
        currentOrderViewModel
                .getCurrentOrderLoadingError()
                .observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Log.d("CONFIRMMMM", "error:** "+s);
                        Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showErrorGetCurrentOrderDialog(String msg) {
        errorGetCurrentOrderDialog = Dialogs.showCustomDialog(getActivity(),
                msg, getString(R.string.order),
                getString(R.string.retry), getString(R.string.finish_work), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        errorGetCurrentOrderDialog.dismiss();
                        getCurrentOrder();

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewOrderActivity.finishWork();
                    }
                });
        Log.d(TAG, "onFailure: " + msg);
    }

    private void fillFieldsWithOrderData(CurrentOrderResponse orderResponse) {

        Order order = orderResponse.getOrder();
        status.setText(order.getOrderStatus());
        client_name.setText(order.getClientName());
        client_address.setText(order.getClientAddress());
        client_area.setText(order.getClientArea());
        shipping_status.setText(order.getOrderShippingStatus());
        client_order_phone1.setText(order.getPhone1());
        order_id.setText(order.getId());
        sender_name.setText(order.getSenderName());
        order_type.setText(order.getOrderType());
        client_feedback.setText(order.getClientFeedback());
        notes.setText(order.getNotes());
        discount.setText(order.getDiscount());
        shipping_cost.setText(order.getShippingCost());

        Log.d(TAG, "fillFieldsWithOrderData: "+orderResponse.getOrder().getClientCity() +" >> "+order.getShippingCost() +"  --  "+order.getTotalOrderCost());
        //order_total_cost.setText(order.getTotalOrderCost());

        initCities(orderResponse);
        getSingleOrderProducts();


        orderProductsAdapter=new OrderProductsAdapter(getContext()
                ,orderResponse.getOrder().getProducts(),this);
        productsRecycler.setAdapter(orderProductsAdapter);
        productsRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));

        calculateOrderTotal();
        updateOrderTotal();
    }


    private void getSingleOrderProducts() {
        BaseClient.getBaseClient().create(Api.class)
                .getSingleOrderProducts(SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                //.getSingleOrderProducts("YIXRKEsDUv4VpAP5BaroqlJb",
                        //null
                        CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId()
                ).enqueue(new Callback<SingleOrderProductsResponse>() {
            @Override
            public void onResponse(Call<SingleOrderProductsResponse> call, Response<SingleOrderProductsResponse> response) {
                SingleOrderProductsResponse singleOrderProductsResponse=response.body();
                if (singleOrderProductsResponse != null) {

                    CurrentOrderData.getInstance().setSingleOrderProductsResponse(singleOrderProductsResponse);
                } else {
                    //TODO make dialog
                }
            }

            @Override
            public void onFailure(Call<SingleOrderProductsResponse> call, Throwable t) {

            }
        });
    }


    private void initCities(CurrentOrderResponse order) {

        ArrayList<String> citiesNames = new ArrayList<>();
        for (City city : order.getCities()) {
            citiesNames.add(city.getCityName());
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.layout_cities_spinner_item, citiesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        client_city.setAdapter(adapter);
        int cityIndex = citiesNames.indexOf(order.getOrder().getClientCity());
        if (cityIndex < 0) {
            cityIndex = 0;
        }
        client_city.setSelection(cityIndex);
        client_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                client_city.setTag(CurrentOrderData.getInstance().getCurrentOrderResponse()
                        .getCities().get(client_city.getSelectedItemPosition()).getCityId());
                Log.d(TAG, "onItemSelected: " + position);
                Log.d(TAG, "onItemSelected: " + client_city.getSelectedItemPosition());

                if (!citiesNames.get(position).equals(CurrentOrderData.getInstance()
                        .getCurrentOrderResponse().getOrder().getClientCity())) {

                    ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));
                    if (CurrentOrderData.getInstance()
                            .getCurrentOrderResponse().getOrder().getOrderType().equals(OrderProductsType.SingleOrder.getType())) {
                        updateSingleOrderData(progressDialog);
                    } else {
                        updateOrderMultiOrderData(progressDialog);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void calculateOrderTotal(){
        int discountValue;
        try {
             discountValue=Integer.valueOf(discount.getText().toString());
        }catch (Exception e){
            discountValue=0;
        }

        int shipping=Integer.valueOf(shipping_cost.getText().toString());
         int orderTotal=orderProductsAdapter.getOrderTotal();
        Log.d("TOTALLLLLLL", "calculateOrderTotal: "+orderTotal);

            if (orderTotal==0){
                Toast.makeText(getContext(), R.string.no_products, Toast.LENGTH_SHORT).show();
                order_total_cost.setText("0");

                return;
            }
            
             orderTotal+=shipping;
            
            if (discountValue > 0 && discountValue >= orderTotal) {
                Toast.makeText(getContext(), R.string.over_discount_warning, Toast.LENGTH_SHORT).show();
                order_total_cost.setText(String.valueOf(orderTotal));
                return;
            }
            orderTotal-=discountValue;
            order_total_cost.setText(String.valueOf(orderTotal));







    }

    private void updateOrderTotal(){
        Log.d("MULTIIIORDERR", "updateOrderTotal");

        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, i+" "+i1+" "+i2+" onTextChanged: "+charSequence);

                calculateOrderTotal();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        discount.addTextChangedListener(textWatcher);

    }

    // init hide and show labels
    private void initLabels() {
        LinearLayout linearLayout = mainView.findViewById(R.id.order_fields);

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (!(linearLayout.getChildAt(i) instanceof LinearLayout)) return;

            LinearLayout chiledLinearLayout = linearLayout.findViewById(linearLayout.getChildAt(i).getId());
            if (chiledLinearLayout != null)
                chiledLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runAnimation(chiledLinearLayout.getChildAt(0).getId(), chiledLinearLayout.getChildAt(1).getId());
                    }
                });
        }
    }

    // animation for show and hide fields labels
    private void runAnimation(int id1, int id2) {

        TextView tv = mainView.findViewById(id1);
        FrameLayout bg = mainView.findViewById(id2);

        if (tv.getVisibility() == View.VISIBLE) {
            Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_sheet_fad_out);
            a.reset();
            tv.clearAnimation();
            tv.startAnimation(a);
            tv.setVisibility(View.GONE);
            bg.setBackgroundResource(R.drawable.ic_background_gray);
        } else {
            Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_sheet_fad_in);
            a.reset();
            tv.clearAnimation();
            tv.startAnimation(a);
            tv.setVisibility(View.VISIBLE);
            bg.setBackgroundResource(R.drawable.ic_background_gray_down);

        }

    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(normal_busy);
            ViewAnimation.showIn(normal_client_cancel);
            ViewAnimation.showIn(normal_client_phone_error);
            ViewAnimation.showIn(normal_delay);
            ViewAnimation.showIn(normal_no_answer);
            ViewAnimation.showIn(normal_order_data_confirmed);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(normal_busy);
            ViewAnimation.showOut(normal_client_cancel);
            ViewAnimation.showOut(normal_client_phone_error);
            ViewAnimation.showOut(normal_delay);
            ViewAnimation.showOut(normal_no_answer);
            ViewAnimation.showOut(normal_order_data_confirmed);
            back_drop.setVisibility(View.GONE);
        }
    }

    private void toggleFabModeShipper(View v) {
        rotateshipper = ViewAnimation.rotateFab(v, !rotateshipper);
        if (rotateshipper) {
            ViewAnimation.showIn(return_order);
            ViewAnimation.showIn(shipping_no_answer);
            ViewAnimation.showIn(deliver);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(return_order);
            ViewAnimation.showOut(shipping_no_answer);
            ViewAnimation.showOut(deliver);
            back_drop.setVisibility(View.GONE);
        }
    }

    private void updateProgress() {
        BaseClient.getBaseClient().create(Api.class)
                .getRemainingOrders(SharedHelper.getKey(getActivity(), LoginActivity.TOKEN))
                .enqueue(new Callback<RemainingOrdersResponse>() {
                    @Override
                    public void onResponse(Call<RemainingOrdersResponse> call, Response<RemainingOrdersResponse> response) {
                        if (response.body() != null) {
                            Log.d("REMAIIGNN", ""+response.body().getInfo()
                            +" "+response.body().getCode()
                                            +"  "+response.body().getData()
                                            +"  "+response.body().getInfo()
                                    +"  "+firstOrder
                            );
                            if (!firstOrder) {
                                firstOrder = true;
                                firstRemaining = response.body().getData();
                                mProgressBar4.setMax(firstRemaining);
                            }

                            int b = firstRemaining - response.body().getData();
                            mProgressBar4.setProgress(b);
                            String remaining = getString(R.string.remaining) + " ( " + NumberFormat.getNumberInstance(Locale.US).format(response.body().getData()) + " ) " + getString(R.string.order);
                            present.setText(remaining);
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            if (sharedPreferences.getBoolean("autoNotifiction", false))
                                createNotification(String.valueOf(firstRemaining - b));

                        }
                    }

                    @Override
                    public void onFailure(Call<RemainingOrdersResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public void createNotification(String first) {

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = null;

            channel = new NotificationChannel("5", "eslam", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("5");
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "5")
                    .setSmallIcon(getActivity().getApplicationInfo().icon)
                    .setContentTitle("orders")
                    .setOngoing(true)
                    .setColor(Color.RED)
                    .addAction(R.drawable.ic_call_end_red, getResources().getString(R.string.start_work),
                            PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(),
                                    NewOrderActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                                    | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0))
                    .setContentText(getString(R.string.remaining) + " " + first + " " + getString(R.string.order))
                    .setSmallIcon(R.drawable.ic_launcher);


            notificationManager.notify(5, builder.build());

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                    .setSmallIcon(getActivity().getApplicationInfo().icon)
                    .setContentTitle("orders")
                    .setOngoing(true)
                    .setColor(Color.RED)
                    .addAction(R.drawable.ic_call_end_red, getResources().getString(R.string.start_work),
                            PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(),
                                    NewOrderActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                                    | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0))
                    .setContentText(getString(R.string.remaining) + " " + first + " " + getString(R.string.order))
                    .setSmallIcon(R.drawable.ic_launcher);


            notificationManager.notify(5, builder.build());
        }


    }



    @Override
    public void onReasonSubmitted(int reason) {

        normalUpdateOrder(OrderUpdateStatusEnums.client_cancel.name());
        String token=SharedHelper.getKey(getContext(),LoginActivity.TOKEN);
        currentOrderViewModel
                .addReasonToOrder(token,String.valueOf(orderId),String.valueOf(reason));
        observeAddingReasonToOrder();
        observeIsReasonAddingToOrder();
        observeAddingReasonToOrderError();
        cancelOrderDialog.dismiss();

    }

    private ProgressDialog showProgressDialog(Context activity, String msg) {
        if (progressDialog!=null){
            progressDialog.show();
            return progressDialog;
        }
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    @Override
    public void onCartItemsChange() {
        calculateOrderTotal();
        //todo handle case of no items
    }

    @Override
    public void OnProductItemClicked(OrderProduct product) {
        ProductDetailDialog productDetailDialog=new ProductDetailDialog(product,this);
        if (getFragmentManager() != null)
            productDetailDialog.show(getFragmentManager(),"");
    }

    @Override
    public void onProductUpdated(OrderProduct product,String productId) {
        calculateOrderTotal();
        orderProductsAdapter.updateProduct(productId,product);
    }

    @Override
    public void onProductAdded(OrderProduct product) {
        boolean wasAdded=orderProductsAdapter.addProduct(product);
        if (wasAdded){
            Toast.makeText(getContext(), R.string.product_added_to_cart, Toast.LENGTH_SHORT).show();
            addProductDialog.dismiss();
        }else {
            Toast.makeText(getContext(), R.string.already_in_cart, Toast.LENGTH_SHORT).show();

        }
    }
}
