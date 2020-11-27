package com.tajr.tajr.fragments;

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
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tajr.tajr.R;
import com.tajr.tajr.activities.LoginActivity;
import com.tajr.tajr.adapters.OrderProductsAdapter;
import com.tajr.tajr.databinding.CurrentOrderFragBinding;
import com.tajr.tajr.helper.CallTimeManager;
import com.tajr.tajr.helper.FabUtil;
import com.tajr.tajr.helper.NetworkUtil;
import com.tajr.tajr.helper.SessionManager;
import com.tajr.tajr.helper.SharedHelper;
import com.tajr.tajr.models.CallActivity;
import com.tajr.tajr.models.CallTimePayload;
import com.tajr.tajr.models.CallTimeResponse;
import com.tajr.tajr.models.DeleteAddProductResponse;
import com.tajr.tajr.models.OrderProduct;
import com.tajr.tajr.models.UpdateOrderNewResponse;
import com.tajr.tajr.models.CurrentOrderData;
import com.tajr.tajr.activities.NewOrderActivity;
import com.tajr.tajr.enums.OrderProductsType;
import com.tajr.tajr.enums.OrderUpdateStatusEnums;
import com.tajr.tajr.enums.ResponseCodeEnums;
import com.tajr.tajr.models.City;
import com.tajr.tajr.models.CurrentOrderResponse;
import com.tajr.tajr.models.Order;
import com.tajr.tajr.models.SingleOrderProductsResponse;
import com.tajr.tajr.server.Api;
import com.tajr.tajr.server.BaseClient;
import com.tajr.tajr.sheets.FragmentBottomSheetDialogFull;
import com.tajr.tajr.view.dialogs.AddProductDialog;
import com.tajr.tajr.view.dialogs.CancelOrderDialog;
import com.tajr.tajr.view.dialogs.Dialogs;
import com.tajr.tajr.view.dialogs.ProductDetailDialog;
import com.tajr.tajr.viewmodels.CurrentOrderViewModel;
import com.tapadoo.alerter.Alerter;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.tajr.tajr.activities.LoginActivity.IS_LOGIN;

public class CurrentOrderFragment extends Fragment
        implements CancelOrderDialog.OnReasonSubmitted
        ,OrderProductsAdapter.OnProductItemEvent
        ,ProductDetailDialog.OnProductUpdated
        ,AddProductDialog.OnProductAdded{

    private final String TAG = "CurrentOrderFragment";
    private CurrentOrderFragBinding binding;
    // main view of the CurrentOrderFragment
    private View mainView;
    private boolean firstOrder;
    private int firstRemaining;
    private Dialog errorGetCurrentOrderDialog;
    private ProgressDialog progressDialog;

    private boolean rotate = false;
    private boolean rotateshipper = false;
    private CurrentOrderViewModel currentOrderViewModel;
    private CancelOrderDialog cancelOrderDialog;
    private CancelOrderDialog.OnReasonSubmitted onReasonSubmitted=this;
    private long orderId=-1;
    private OrderProductsAdapter orderProductsAdapter;
    private AddProductDialog.OnProductAdded onProductAdded;
    private AddProductDialog addProductDialog;
    private String token= SessionManager.getInstance(getContext()).getToken();

    private FabUtil fabUtil;
    public CurrentOrderFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= DataBindingUtil.inflate(
                inflater,R.layout.current_order_frag, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, binding.getRoot());

        onProductAdded= this;
        CardView[] buttons={binding.normalBusy,binding.normalClientCancel,binding.normalClientPhoneError
                ,binding.normalDelay,binding.normalNoAnswer,binding.normalOrderDataConfirmed};
        CardView[] shipperButtons={binding.returnOrder,binding.shippingNoAnswer,binding.deliver};

        fabUtil=new FabUtil(buttons,shipperButtons,binding.backDrop);

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

        return binding.getRoot();
    }

    private void setListeners() {
        binding.addProduct.setOnClickListener(v -> {
            //addProductToMultiOrdersTv();
            addProductDialog=new AddProductDialog(onProductAdded);
            if (getFragmentManager() != null) {
                addProductDialog.show(getFragmentManager(),"");
            }
        });
        binding.backDrop.setOnClickListener(v -> {
            binding.backDrop.setVisibility(View.GONE);
            fabUtil.toggle(binding.normalUpdateButton);
        });

        binding.normalUpdateButton.setOnClickListener(v -> {
            fabUtil.toggle(v);
        });
        binding.normalUpdateButtonShipping.setOnClickListener(v -> fabUtil.toggleFabModeShipper(binding.normalUpdateButtonShipping));

        binding.normalBusy.setOnClickListener(v -> {
            fabUtil.toggle(binding.normalUpdateButton);
            normalUpdateOrder(OrderUpdateStatusEnums.client_busy.name());
        });

        binding.normalClientCancel.setOnClickListener(v -> {
            fabUtil.toggle(binding.normalUpdateButton);
            cancelOrderDialog=new CancelOrderDialog(onReasonSubmitted);
            cancelOrderDialog.show(getChildFragmentManager(),"CANCEL");
            //normalUpdateOrder(OrderUpdateStatusEnums.client_cancel.name());
        });

        binding.normalClientPhoneError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabUtil.toggle(binding.normalUpdateButton);
                normalUpdateOrder(OrderUpdateStatusEnums.client_phone_error.name());
            }
        });

        binding.normalNoAnswer.setOnClickListener(v -> {
            fabUtil.toggle(binding.normalUpdateButton);
            normalUpdateOrder(OrderUpdateStatusEnums.client_noanswer.name());
        });

        binding.normalOrderDataConfirmed.setOnClickListener(v -> {
            fabUtil.toggle(binding.normalUpdateButton);
            updateClientData();
        });

        binding.normalDelay.setOnClickListener(v -> {
            fabUtil.toggle(binding.normalUpdateButton);
            chooseDate();
        });


        binding.deliver.setOnClickListener(v -> {
            fabUtil.toggleFabModeShipper(binding.normalUpdateButtonShipping);
            updateShippingOrder("deliver");
        });

        binding.returnOrder.setOnClickListener(v -> {
            fabUtil.toggleFabModeShipper(binding.normalUpdateButtonShipping);
            updateShippingOrder("return");
        });


        binding.shippingNoAnswer.setOnClickListener(v -> {
            fabUtil.toggleFabModeShipper(binding.normalUpdateButtonShipping);
            updateShippingOrder("client_noanswer");
        });

    }

    private void observeAddingReasonToOrder(){
        currentOrderViewModel
                .addReasonToOrder()
        .observe(getActivity(), mainResponse ->
                Toast.makeText(getContext(), mainResponse.getData(), Toast.LENGTH_LONG).show());
    }
    private void observeIsReasonAddingToOrder(){
        currentOrderViewModel
                .getIsReasonAddingTOOrder()
                .observe(getActivity(), aBoolean ->
                        Log.d("REASONORDER", "onChanged: "+aBoolean));
    }
    private void observeAddingReasonToOrderError(){
        currentOrderViewModel
                .getReasonAddingToOrderError()
                .observe(getActivity(), s ->
                        Toast.makeText(getContext(), R.string.adding_reason_to_order_error, Toast.LENGTH_SHORT).show()
                );
    }

    private void updateShippingOrder(String action) {
        ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));
        Api api = BaseClient.getBaseClient().create(Api.class);
        api.updateShippingOrders(token, CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                action, CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId()
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



    private void updateClientData() {
        ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));
        BaseClient.getBaseClient().create(Api.class).updateClientData(
                token,
                CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                binding.clientName.getText().toString(),
                binding.clientAddress.getText().toString(),
                binding.clientArea.getText().toString(),
                binding.ntes.getText().toString()
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
                showErrorGetCurrentOrderDialog(getString(R.string.error_updating_client_data));
            }
        });
    }

    private void updateSingleOrderData(ProgressDialog progressDialog) {
        BaseClient.getBaseClient().create(Api.class).updateSingleOrderData(
                token,CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                orderProductsAdapter.getProducts().get(0).getId(),
                binding.clientCity.getTag().toString(),
                String.valueOf(orderProductsAdapter.getProducts().get(0).getItems_no()),
                binding.discount.getText().toString().trim()
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
                showErrorGetCurrentOrderDialog(getString(R.string.error_updating_order_data));
            }
        });
    }

    private void updateOrderMultiOrderData(ProgressDialog progressDialog) {
        BaseClient.getBaseClient().create(Api.class).updateOrderMultiOrderData(
                token,CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                binding.clientCity.getTag().toString(),
                binding.discount.getText().toString().trim()
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
                showErrorGetCurrentOrderDialog(getString(R.string.error_updating_order_data));



            }
        });
    }

    private void normalUpdateOrder(String status) {
        currentOrderViewModel
                .updateOrder(token,
                        CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                        CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                        status)
                .observe(this, updateOrderNewResponse -> {
                    if (updateOrderNewResponse!=null){
                        Log.d("CONFIRMMMM", "onChanged: "+updateOrderNewResponse.getData());
                        handleCallTime(updateOrderNewResponse.getOrder_id(),updateOrderNewResponse.getHistory_line());
                        getCurrentOrder();
                    }else {
                        Log.d("CONFIRMMMM", "error: --------------");

                    }
                });
        observeOrderUpdating();
        observeOrderUpdatingError();

    }

    private void observeOrderUpdating(){
        currentOrderViewModel
                .getIsOrderUpdating()
                .observe(getActivity(), aBoolean -> {
                    Log.d("DDIAALOOOOGGG", "order update: "+aBoolean);
                    if (aBoolean!=null&&aBoolean)
                        progressDialog=showProgressDialog(getActivity(), getString(R.string.fetching_th_order));
                    else
                        progressDialog.dismiss();

                });
    }

    private void observeOrderUpdatingError(){
        currentOrderViewModel
                .getOrderUpdatingError()
                .observe(getActivity(), s ->
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show());
    }

    private void handleCallTime(String order_id,String history_line){
        ArrayList<CallActivity> callActivity=CallTimeManager.getInstance(getContext())
                .getCallActivity();

        if (callActivity==null||callActivity.isEmpty()){
            Log.d("handleCallTime", "empty:" );
            return;
        }
        for (CallActivity activity:callActivity) {
            activity.setHistory_line(history_line);
        }
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

                    currentOrderViewModel.updateDelayedOrders(
                            CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                            dateString,
                            CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                            OrderUpdateStatusEnums.client_delay.name()
                    ).observe(this, response -> {
                        getCurrentOrder();
                        Log.d("DELAYYYYY", "onResponse: " + response.toString());
                    });

                    if (!currentOrderViewModel.getDelayedOrdersUpdatingError().hasObservers())
                    currentOrderViewModel.getDelayedOrdersUpdatingError()
                            .observe(this, s -> {
                                Log.d("DELAYYYYY", "onFailure: " + s);
                                showErrorGetCurrentOrderDialog(s);
                            });

                    if (!currentOrderViewModel.getIsDelayedOrdersUpdating().hasObservers())
                    currentOrderViewModel.getIsDelayedOrdersUpdating()
                            .observe(this, aBoolean -> {
                                if (aBoolean!=null&&aBoolean){
                                    progressDialog = showProgressDialog(getActivity(),getString(R.string.fetching_th_order));
                                }else {
                                    progressDialog.dismiss();
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
                token,
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
                                    getActivity().finish();
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
                binding.normalUpdateActions.setVisibility(View.VISIBLE);
                binding.shipperUpdateActions.setVisibility(View.GONE);
            } else {
                fillFieldsWithOrderData(CurrentOrderData.getInstance().getMissedCallOrderResponse());
                updateProgress();
                binding.normalUpdateActions.setVisibility(View.GONE);
                binding.shipperUpdateActions.setVisibility(View.VISIBLE);

            }
        } catch (Exception e) {
            Log.e("eslamfaissal", "onResponse: ", e);
            Log.d("eslamfaissal", "onResponse: " + CurrentOrderData.getInstance().getMissedCallOrderResponse().toString());
            CurrentOrderData.getInstance().setCurrentOrderResponse(CurrentOrderData.getInstance().getMissedCallOrderResponse());
            fillFieldsWithOrderData(CurrentOrderData.getInstance().getMissedCallOrderResponse());
            updateProgress();
            binding.normalUpdateActions.setVisibility(View.VISIBLE);
            binding.shipperUpdateActions.setVisibility(View.GONE);
        }


    }

    private void getCurrentOrder(){

        if(!NetworkUtil.isConnected(getContext())){
            Alerter.create(getActivity())
                    .enableSwipeToDismiss()
                    .setBackgroundResource(R.color.primary)
                    .setDuration(2500)
                    .setText(getString(R.string.no_connection_message))
                    .show();
            return;
        }
        Log.d("CONFIRMMMM", " getting CurrentOrder "+token);

        currentOrderViewModel
                .getCurrentOrder(token)
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
                                        binding.normalUpdateActions.setVisibility(View.VISIBLE);
                                        binding.shipperUpdateActions.setVisibility(View.GONE);
                                    } else {
                                        fillFieldsWithOrderData(currentOrderResponse);
                                        updateProgress();
                                        binding.normalUpdateActions.setVisibility(View.GONE);
                                        binding.shipperUpdateActions.setVisibility(View.VISIBLE);

                                    }
                                } catch (Exception e) {
                                    Log.e("eslamfaissal", "onResponse: ", e);
                                    Log.d("eslamfaissal", "onResponse: " + currentOrderResponse.toString());
                                    CurrentOrderData.getInstance().setCurrentOrderResponse(currentOrderResponse);
                                    fillFieldsWithOrderData(currentOrderResponse);
                                    updateProgress();
                                    binding.normalUpdateActions.setVisibility(View.VISIBLE);
                                    binding.shipperUpdateActions.setVisibility(View.GONE);
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
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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
                        getActivity().finish();
                    }
                });
        Log.d(TAG, "onFailure: " + msg);
    }

    private void fillFieldsWithOrderData(CurrentOrderResponse orderResponse) {

        Order order = orderResponse.getOrder();
        binding.setOrder(order);



        initCities(orderResponse);
        getSingleOrderProducts();


        orderProductsAdapter=new OrderProductsAdapter(getContext()
                ,orderResponse.getOrder().getProducts(),this);
        binding.productsRecycler.setAdapter(orderProductsAdapter);
        binding.productsRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
        binding.productsRecycler.startLayoutAnimation();

        calculateOrderTotal();
        updateOrderTotal();
    }


    private void getSingleOrderProducts() {
        BaseClient.getBaseClient().create(Api.class)
                .getSingleOrderProducts(token,
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
        binding.clientCity.setAdapter(adapter);
        int cityIndex = citiesNames.indexOf(order.getOrder().getClientCity());
        if (cityIndex < 0) {
            cityIndex = 0;
        }
        binding.clientCity.setSelection(cityIndex);
        binding.clientCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.clientCity.setTag(CurrentOrderData.getInstance().getCurrentOrderResponse()
                        .getCities().get(binding.clientCity.getSelectedItemPosition()).getCityId());
                Log.d(TAG, "onItemSelected: " + position);
                Log.d(TAG, "onItemSelected: " + binding.clientCity.getSelectedItemPosition());

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
        double discountValue;
        try {
             discountValue=Double.valueOf(binding.discount.getText().toString());
        }catch (Exception e){
            discountValue=0;
        }
        int shipping;
                try {
                    shipping=Integer.valueOf(binding.shippingCost.getText().toString());

                }catch (Exception e){
                    shipping=0;
                }
         int orderTotal=orderProductsAdapter.getOrderTotal();
            if (orderTotal==0){
                Toast.makeText(getContext(), R.string.no_products, Toast.LENGTH_SHORT).show();
                binding.orderTotalCost.setText("0");
                return;
            }
             orderTotal+=shipping;
            if (discountValue > 0 && discountValue >= orderTotal) {
                Toast.makeText(getContext(), R.string.over_discount_warning, Toast.LENGTH_SHORT).show();
                binding.orderTotalCost.setText(String.valueOf(orderTotal));
                return;
            }
            orderTotal-=discountValue;
            binding.orderTotalCost.setText(String.valueOf(orderTotal));
    }

    private void updateOrderTotal(){
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
        binding.discount.addTextChangedListener(textWatcher);

    }

    private void updateProgress() {
        currentOrderViewModel
                .getRemainingOrders()
                .observe(this, remainingOrdersResponse -> {
                    if (!firstOrder) {
                        firstOrder = true;
                        firstRemaining = remainingOrdersResponse.getData();
                        binding.ProgressBar.setMax(firstRemaining);
                    }

                    int b = firstRemaining - remainingOrdersResponse.getData();
                    binding.ProgressBar.setProgress(b);
                    String remaining = getString(R.string.remaining)
                            + " ( " + NumberFormat.getNumberInstance(Locale.US).format(remainingOrdersResponse.getData())
                            + " ) " + getString(R.string.order);
                    binding.present.setText(remaining);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    if (sharedPreferences.getBoolean("autoNotifiction", false))
                        createNotification(String.valueOf(firstRemaining - b));
                });

        if (!currentOrderViewModel.getRemainingOrdersLoadingError().hasObservers())
            currentOrderViewModel.getRemainingOrdersLoadingError()
            .observe(this, error -> {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            });



    }

    public void createNotification(String first) {

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel;

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
//        if (getFragmentManager() != null)
//            productDetailDialog.show(getFragmentManager(),"");

        FragmentBottomSheetDialogFull fragment = new FragmentBottomSheetDialogFull(product, this);
        fragment.show(getChildFragmentManager(), fragment.getTag());
    }

    @Override
    public void onProductUpdated(OrderProduct product,String productId) {
        Log.d("EXTRAAAAAAA", "onProductUpdated: "+product.getExtras().toString());
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
