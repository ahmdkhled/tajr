package com.greyeg.tajr.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.ViewAnimation;
import com.greyeg.tajr.models.DeleteAddProductResponse;
import com.greyeg.tajr.models.UpdateOrderNewResponse;
import com.greyeg.tajr.models.CurrentOrderData;
import com.greyeg.tajr.activities.NewOrderActivity;
import com.greyeg.tajr.adapters.MultiOrderProductsAdapter;
import com.greyeg.tajr.adapters.SignleOrderProductsAdapter;
import com.greyeg.tajr.enums.OrderProductsType;
import com.greyeg.tajr.enums.OrderUpdateStatusEnums;
import com.greyeg.tajr.enums.ResponseCodeEnums;
import com.greyeg.tajr.models.City;
import com.greyeg.tajr.models.CurrentOrderResponse;
import com.greyeg.tajr.models.Order;
import com.greyeg.tajr.models.Product;
import com.greyeg.tajr.models.SingleOrderProductsResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.view.dialogs.Dialogs;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
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
import static com.greyeg.tajr.view.dialogs.Dialogs.showProgressDialog;

public class SearchOrderFragment extends Fragment {

    private final String TAG = "CurrentOrderFragment";
    @BindView(R.id.client_name)
    EditText client_name;
    @BindView(R.id.client_address)
    EditText client_address;
    @BindView(R.id.client_area)
    EditText client_area;
    @BindView(R.id.item_no)
    EditText item_no;
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
    @BindView(R.id.item_cost)
    EditText item_cost;
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
    @BindView(R.id.single_order_product_spinner)
    Spinner single_order_product_spinner;

    @BindView(R.id.back_drop)
    View back_drop;
    // multi orders
    @BindView(R.id.products_recycler_view)
    RecyclerView multiOrderroductsRecyclerView;
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


    @BindView(R.id.save_edit)
    FloatingActionButton save_edit;
    @BindView(R.id.deliver)
    CardView deliver;
    @BindView(R.id.shipping_no_answer)
    CardView shipping_no_answer;
    @BindView(R.id.return_order)
    CardView return_order;

    @BindView(R.id.phone_input)
    EditText phoneInput;
    // main view of the CurrentOrderFragment
    private View mainView;
    private LinearLayoutManager multiOrderProductsLinearLayoutManager;
    private MultiOrderProductsAdapter multiOrderProductsAdapter;
    private boolean firstOrder;
    private int firstRemaining;
    private Dialog errorGetCurrentOrderDialog;
    private boolean rotate = false;
    private boolean rotateshipper = false;
    private boolean productExbandable = false;

    public SearchOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_search_order, container, false);
        ButterKnife.bind(this, mainView);
        initLabels();

        setListeners();
        return mainView;
    }

    @OnClick(R.id.clear)
    void clearSearch() {
        phoneInput.setText("");
    }

    private void setListeners() {
        phoneInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (phoneInput.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "برجاء ادخال رقم الهاتف", Toast.LENGTH_SHORT).show();
                } else {
                    getCurrentOrder();
                }
                return true;
            }
            return false;
        });
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToMultiOrdersTv();
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
                normalUpdateOrder(OrderUpdateStatusEnums.client_cancel.name());
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
                normalUpdateOrder(OrderUpdateStatusEnums.order_data_confirmed.name());
            }
        });

        normal_delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(normalUpdateButton);
                chooseDate();
            }
        });
        save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClientData();
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

    @OnClick(R.id.search)
    void searchNow() {
      getCurrentOrder();
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
                getActivity().onBackPressed();
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {
                progressDialog.dismiss();
                getCurrentOrder();
                Log.d(TAG, "onResponse: " + t.getMessage());
            }
        });
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
                if (CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getOrderType().equals(OrderProductsType.SingleOrder.getType())) {
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
                single_order_product_spinner.getTag().toString(),
                client_city.getTag().toString(),
                item_no.getText().toString().trim(),
                discount.getText().toString().trim()
        ).enqueue(new Callback<CurrentOrderResponse>() {
            @Override
            public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                progressDialog.dismiss();
                getActivity().onBackPressed();
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
                getActivity().onBackPressed();
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
        ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));

        BaseClient.getBaseClient().create(Api.class).updateOrders(
                SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                status
        )
                .enqueue(new Callback<UpdateOrderNewResponse>() {
                    @Override
                    public void onResponse(Call<UpdateOrderNewResponse> call, Response<UpdateOrderNewResponse> response) {
                        progressDialog.dismiss();
                        getActivity().onBackPressed();
                        Log.d(TAG, "onResponse: " + response.toString());
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        showErrorGetCurrentOrderDialog(t.getMessage());
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
                            getActivity().onBackPressed();
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

    void addProductToMultiOrdersTv() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_add_poduct_dialog);

//        Spinner productSpinner = dialog.findViewById(R.id.product_spinner);
  //      productSpinner.setTag(OrderProductsType.MuhltiOrder.getType());
        EditText productNo = dialog.findViewById(R.id.product_no);
        TextView addProductBtn = dialog.findViewById(R.id.add_product);
        productNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        //fillSpinnerWithProduts(productSpinner);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //addProductToMultiOrder(Integer.valueOf(productNo.getText().toString()), productSpinner.getSelectedItemPosition());
            }
        });
        dialog.show();

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

    private void getCurrentOrder() {

        ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));
        BaseClient.getBaseClient().create(Api.class).
                getPhoneData2(
                        SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                        SharedHelper.getKey(getActivity(), LoginActivity.USER_ID),
                        phoneInput.getText().toString()

                )
                .enqueue(new Callback<CurrentOrderResponse>() {
                    @Override
                    public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                        progressDialog.dismiss();
                        if (response.body() != null) {
                            if (response.body().getCode().equals(ResponseCodeEnums.code_1200.getCode())) {
                                CurrentOrderData.getInstance().setCurrentOrderResponse(response.body());

                                try {
                                    if (CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getCheckType().equals("normal_order")) {
                                        fillFieldsWithOrderData(response.body());

                                        productExbandable = true;
                                        normal_update_actions.setVisibility(View.VISIBLE);
                                        shipper_update_actions.setVisibility(View.GONE);
                                    } else {
                                        fillFieldsWithOrderData(response.body());

                                        productExbandable = true;
                                        normal_update_actions.setVisibility(View.GONE);
                                        shipper_update_actions.setVisibility(View.VISIBLE);

                                    }
                                } catch (Exception e) {
                                    Log.e("eslamfaissal", "onResponse: ", e);
                                    Log.d("eslamfaissal", "onResponse: " + response.body().toString());
                                    CurrentOrderData.getInstance().setCurrentOrderResponse(response.body());
                                    fillFieldsWithOrderData(response.body());

                                    productExbandable = true;
                                    normal_update_actions.setVisibility(View.VISIBLE);
                                    shipper_update_actions.setVisibility(View.GONE);
                                }


                            } else if (response.body().getCode().equals(ResponseCodeEnums.code_1300.getCode())) {
                                // no new orders all handled
                                Dialogs.showCustomDialog(getActivity(), getString(R.string.no_more_orders), getString(R.string.order),
                                        "Back", null, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                NewOrderActivity.finishWork();
                                            }
                                        }, null);
                            } else if (
                                    response.body().getCode().equals(ResponseCodeEnums.code_1407.getCode()) ||
                                            response.body().getCode().equals(ResponseCodeEnums.code_1408.getCode()) ||
                                            response.body().getCode().equals(ResponseCodeEnums.code_1490.getCode()) ||
                                            response.body().getCode().equals(ResponseCodeEnums.code_1511.getCode()) ||
                                            response.body().getCode().equals(ResponseCodeEnums.code_1440.getCode())) {

                                SharedHelper.putKey(getActivity(), IS_LOGIN, "no");
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                NewOrderActivity.finishWork();
                            }

                        } else {
                            showErrorGetCurrentOrderDialog(response.toString());
                            Log.d(TAG, "onResponse: null = " + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        showErrorGetCurrentOrderDialog(t.getMessage());

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
        item_cost.setText(order.getItemCost());
        if (!order.getItemsNo().equals("0"))
            item_no.setText(order.getItemsNo());
        else
            item_no.setText("1");
        order_total_cost.setText(order.getTotalOrderCost());
        sender_name.setText(order.getSenderName());
        order_type.setText(order.getOrderType());
        client_feedback.setText(order.getClientFeedback());
        notes.setText(order.getNotes());
        discount.setText(order.getDiscount());
        shipping_cost.setText(order.getShippingCost());

        initCities(orderResponse);
        getSingleOrderProducts();
        if (order.getOrderType().equals(OrderProductsType.SingleOrder.getType())) {
            single_order_product_spinner.setVisibility(View.VISIBLE);
        } else {
            getMultiOrdersProducts();
            single_order_product_spinner.setVisibility(View.GONE);
        }
    }

    private void getMultiOrdersProducts() {
        multiOrderProductsAdapter = new MultiOrderProductsAdapter(getActivity(),
                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getMultiOrders(),
                new MultiOrderProductsAdapter.GetOrderInterface() {
                    @Override
                    public void getOrder() {
                        getCurrentOrder();
                    }
                });
        multiOrderProductsLinearLayoutManager = new LinearLayoutManager(getActivity());
        multiOrderroductsRecyclerView.setLayoutManager(multiOrderProductsLinearLayoutManager);
        multiOrderroductsRecyclerView.setAdapter(multiOrderProductsAdapter);


    }

    private void getSingleOrderProducts() {
        BaseClient.getBaseClient().create(Api.class)
                .getSingleOrderProducts(SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                        CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId()
                ).enqueue(new Callback<SingleOrderProductsResponse>() {
            @Override
            public void onResponse(Call<SingleOrderProductsResponse> call, Response<SingleOrderProductsResponse> response) {
                if (response.body() != null) {
                    CurrentOrderData.getInstance().setSingleOrderProductsResponse(response.body());
                    fillSpinnerWithProduts(single_order_product_spinner);
                } else {
                    //TODO make dialog
                }
            }

            @Override
            public void onFailure(Call<SingleOrderProductsResponse> call, Throwable t) {

            }
        });
    }

    private void fillSpinnerWithProduts(Spinner spinner) {
        if (CurrentOrderData.getInstance().getSingleOrderProductsResponse() != null) {
            ArrayAdapter adapter = new SignleOrderProductsAdapter(getActivity(),
                    CurrentOrderData.getInstance().getSingleOrderProductsResponse());

            spinner.setAdapter(adapter);
            int index = 0;
            for (Product product : CurrentOrderData.getInstance().getSingleOrderProductsResponse().getProducts()) {
                if (product.getProductId().equals(CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getProductId())) {
                    index = CurrentOrderData.getInstance().getSingleOrderProductsResponse().getProducts().indexOf(product);
                }
            }
            spinner.setSelection(index);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spinner.getTag().equals(OrderProductsType.MuhltiOrder.getType())) {
                        Log.d(TAG, "onItemSelected: from multi " + spinner.getSelectedItemPosition());
                    } else {
                        single_order_product_spinner.setTag(CurrentOrderData.getInstance().getSingleOrderProductsResponse().getProducts().get(position).getProductId());
                        Log.d(TAG, "onItemSelected: from single " + spinner.getSelectedItemPosition());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
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

                if (!citiesNames.get(position).equals(CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getClientCity())) {

                    ProgressDialog progressDialog = showProgressDialog(getActivity(), getString(R.string.fetching_th_order));
                    if (CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getOrderType().equals(OrderProductsType.SingleOrder.getType())) {
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

    // init hide and show labels
    private void initLabels() {
        LinearLayout linearLayout = mainView.findViewById(R.id.order_fields);

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
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

}
