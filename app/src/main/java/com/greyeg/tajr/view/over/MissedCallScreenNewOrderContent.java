package com.greyeg.tajr.view.over;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.font.RobotoTextView;
import com.greyeg.tajr.models.CurrentOrderData;
import com.greyeg.tajr.activities.NewOrderActivity;
import com.greyeg.tajr.adapters.MultiOrderProductsAdapter;
import com.greyeg.tajr.adapters.SignleOrderProductsAdapter;
import com.greyeg.tajr.enums.OrderProductsType;
import com.greyeg.tajr.models.City;
import com.greyeg.tajr.models.CurrentOrderResponse;
import com.greyeg.tajr.models.Order;
import com.greyeg.tajr.models.Product;
import com.greyeg.tajr.models.SingleOrderProductsResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.mattcarroll.hover.Content;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MissedCallScreenNewOrderContent implements Content {

    private final Context mContext;
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
    // multi orders
    @BindView(R.id.products_recycler_view)
    RecyclerView multiOrderroductsRecyclerView;
    // update normal order

    @BindView(R.id.edit_order)
    RobotoTextView editOrderBtn;


    // main view of the CurrentOrderFragment
    private LinearLayoutManager multiOrderProductsLinearLayoutManager;
    private MultiOrderProductsAdapter multiOrderProductsAdapter;
    private boolean firstOrder;
    private int firstRemaining;
    private Dialog errorGetCurrentOrderDialog;
    private boolean rotate = false;
    private boolean rotateshipper = false;
    private boolean productExbandable = false;

    private LayoutInflater li;
    private View mContent;

    public MissedCallScreenNewOrderContent(@NonNull Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    @NonNull
    @Override
    public View getView() {

        if (null == mContent) {
            li = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

            mContent = li.inflate(R.layout.layout_missed_call_no_order_found, null);

            ButterKnife.bind(this, mContent);
            onViewCreated(mContent);
            // We present our desire to be non-fullscreen by using WRAP_CONTENT for height.  This
            // preference will be honored by the Hover Menu to make our content only as tall as we
            // want to be.
            mContent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        return mContent;
    }

    @OnClick(R.id.edit_order)
    void editOrder(){
        Intent intent = new Intent(mContext.getApplicationContext(),NewOrderActivity.class);
        intent.putExtra("fromBuble","buble");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
        mContext.stopService(new Intent(mContext, MissedCallOrderService.class));
        MissedCallOrderService.showFloatingMenu(mContext.getApplicationContext());

    }

    private void fillSpinnerWithProduts(Spinner spinner) {
        if (CurrentOrderData.getInstance().getSingleOrderProductsResponse() != null) {
            ArrayAdapter adapter = new SignleOrderProductsAdapter(getActivity(),
                    CurrentOrderData.getInstance().getSingleOrderProductsResponse());

            spinner.setAdapter(adapter);
            int index = 0;
            for (Product product : CurrentOrderData.getInstance().getSingleOrderProductsResponse().getProducts()) {
                if (product.getProductId().equals(CurrentOrderData.getInstance().getMissedCallOrderResponse().getOrder().getProductId())) {
                    index = CurrentOrderData.getInstance().getSingleOrderProductsResponse().getProducts().indexOf(product);
                }
            }
            spinner.setSelection(index);

            spinner.setEnabled(false);
        }
    }

    private void getSingleOrderProducts() {
        BaseClient.getBaseClient().create(Api.class)
                .getSingleOrderProducts(SessionManager.getInstance(mContext).getToken(),
                        CurrentOrderData.getInstance().getMissedCallOrderResponse().getUserId()
                ).enqueue(new Callback<SingleOrderProductsResponse>() {
            @Override
            public void onResponse(Call<SingleOrderProductsResponse> call, Response<SingleOrderProductsResponse> response) {
                if (response.body() != null) {
                    CurrentOrderData.getInstance().setSingleMissedOrderProductsResponse(response.body());
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

    private void getMultiOrdersProducts() {
        multiOrderProductsAdapter = new MultiOrderProductsAdapter(getActivity(),
                CurrentOrderData.getInstance().getMissedCallOrderResponse().getOrder().getMultiOrders(),
                new MultiOrderProductsAdapter.GetOrderInterface() {
                    @Override
                    public void getOrder() {

                    }
                });
        multiOrderProductsLinearLayoutManager = new LinearLayoutManager(getActivity());
        multiOrderroductsRecyclerView.setLayoutManager(multiOrderProductsLinearLayoutManager);
        multiOrderroductsRecyclerView.setAdapter(multiOrderProductsAdapter);


    }

    private Context getActivity() {
        return mContext;
    }

    public void onViewCreated(@NonNull View view) {
        fillFieldsWithOrderData(CurrentOrderData.getInstance().getMissedCallOrderResponse());
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
        client_city.setEnabled(false);
    }

    @Override
    public boolean isFullscreen() {
        return false;
    }

    @Override
    public void onShown() {
        // No-op.
    }

    @Override
    public void onHidden() {
        // No-op.
    }
}
