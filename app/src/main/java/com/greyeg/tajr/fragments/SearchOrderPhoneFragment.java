package com.greyeg.tajr.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.models.SimpleOrderResponse;
import com.greyeg.tajr.models.UpdateOrderNewResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.view.utils.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchOrderPhoneFragment extends Fragment {

    @BindView(R.id.phone_input)
    EditText phoneInput;

    @BindView(R.id.product)
    EditText product;

    @BindView(R.id.client_name)
    EditText client_name;

    @BindView(R.id.client_address)
    EditText client_address;

    @BindView(R.id.client_area)
    EditText client_area;

    @BindView(R.id.client_city)
    EditText client_city;

    @BindView(R.id.item_no)
    EditText item_no;

    @BindView(R.id.client_order_phone1)
    EditText client_order_phone1;

    @BindView(R.id.status)
    EditText status;

    @BindView(R.id.client_order_phone2)
    EditText client_order_phone2;

    @BindView(R.id.sender_name)
    EditText sender_name;

    @BindView(R.id.item_cost)
    EditText item_cost;

    @BindView(R.id.order_cost)
    EditText order_cost;

    @BindView(R.id.order_total_cost)
    EditText order_total_cost;

    @BindView(R.id.shipping_retum_cost)
    EditText shipping_retum_cost;

    @BindView(R.id.real_shipping_return_cost)
    EditText real_shipping_return_cost;

    @BindView(R.id.client_feedback)
    EditText client_feedback;

    @BindView(R.id.order_type)
    EditText order_type;

    @BindView(R.id.order_view)
    View orderView;


    @BindView(R.id.move_listener)
    TextView moveListener;

    @BindView(R.id.no_order_tv)
    TextView noOrder;

    @BindView(R.id.progress_search)
    ProgressWheel progressSearch;


    Api api;
    private SimpleOrderResponse.Order order;
    private String order_ud;
    private String phone;
    ProgressDialog progressDialog;
    private SessionManager sessionManager;

    public SearchOrderPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sessionManager=SessionManager.getInstance(getContext());
        return inflater.inflate(R.layout.fragment_search_order_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        progressSearch.spin();

        api = BaseClient.getBaseClient().create(Api.class);


        phoneInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (phoneInput.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "برجاء ادخال رقم الهاتف", Toast.LENGTH_SHORT).show();
                    } else {
                        search();
                    }
                    return true;
                }
                return false;
            }
        });
    }


    @OnClick(R.id.search)
    void searchNow() {
        if (phoneInput.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "برجاء ادخال رقم الهاتف", Toast.LENGTH_SHORT).show();
        } else {
            search();
        }

    }

    void search() {
        progressSearch.setVisibility(View.VISIBLE);
        noOrder.setVisibility(View.GONE);
        api.getPhoneData(
                sessionManager.getToken(),sessionManager.getUserId(), phoneInput.getText().toString()

        ).enqueue(new Callback<SimpleOrderResponse>() {
            @Override
            public void onResponse(Call<SimpleOrderResponse> call, Response<SimpleOrderResponse> response) {

            }

            @Override
            public void onFailure(Call<SimpleOrderResponse> call, Throwable t) {
                orderView.setVisibility(View.GONE);
                noOrder.setVisibility(View.VISIBLE);
                progressSearch.setVisibility(View.GONE);

                Log.d("eeeeeeeeeeeee", "onFailure: " + t.getMessage());
            }
        });

    }

    @OnClick(R.id.clear)
    void clearSearch() {
        phoneInput.setText("");
    }


    void updateOrder(String value) {
        Api api = BaseClient.getBaseClient().create(Api.class);
        api.updateOrders(
                sessionManager.getToken(),
                order_ud,
                sessionManager.getUserId(),
                value
        ).enqueue(new Callback<UpdateOrderNewResponse>() {
            @Override
            public void onResponse(Call<UpdateOrderNewResponse> call, Response<UpdateOrderNewResponse> response) {
                if (response.body() != null) {
                    Log.d("eeeeeeeeeeeeee", "onResponse: updateOrder" + response.body().getCode());
                }
                if (response.body().getCode().equals("1200") || response.body().getCode().equals("1202")) {
                    Log.d("eeeeeeeeeeeeee", "onResponse: updateOrder" + response.body().getCode());
                    progressDialog.dismiss();

                } else {
                   // onButtonPressed();
                }

            }

            @Override
            public void onFailure(Call<UpdateOrderNewResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("eeeeeeeeeeeeeeee", "onFailure:update order " + t.getMessage());
                //finishTheWorkNow();
            }
        });
    }


}
