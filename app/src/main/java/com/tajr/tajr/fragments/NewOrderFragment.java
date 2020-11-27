package com.tajr.tajr.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tajr.tajr.R;
import com.tajr.tajr.activities.LoginActivity;
import com.tajr.tajr.adapters.ProductSpinnerAdapter;
import com.tajr.tajr.helper.NetworkUtil;
import com.tajr.tajr.helper.SharedHelper;
import com.tajr.tajr.models.AllProducts;
import com.tajr.tajr.models.Cities;
import com.tajr.tajr.models.NewOrderResponse;
import com.tajr.tajr.models.OrderItem;
import com.tajr.tajr.models.OrderPayload;
import com.tajr.tajr.models.ProductData;
import com.tajr.tajr.models.ProductForSpinner;
import com.tajr.tajr.models.CurrentOrderData;
import com.tajr.tajr.server.Api;
import com.tajr.tajr.server.BaseClient;
import com.tajr.tajr.viewmodels.NewOrderFragVM;
import com.tapadoo.alerter.Alerter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;



/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrderFragment extends Fragment {


    @BindView(R.id.product)
    Spinner product;

    @BindView(R.id.client_name)
    EditText client_name;

    @BindView(R.id.client_address)
    EditText client_address;

    @BindView(R.id.client_area)
    EditText client_area;

    @BindView(R.id.client_city)
    Spinner client_city;

    @BindView(R.id.item_no)
    EditText item_no;

    @BindView(R.id.client_order_phone1)
    EditText client_order_phone1;

    private View mainView;

    private String productId;

    private List<ProductForSpinner> products;

    private List<String> cities = new ArrayList<>();
    private List<String> citiesId = new ArrayList<>();
    private ArrayList<OrderItem> orderItems = new ArrayList<>();

    private static String CITY_ID;

    private List<Cities.City> citiesBody;
    private NewOrderFragVM newOrderFragVM;
    Api api;

    public NewOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_new_order, container, false);
        newOrderFragVM= ViewModelProviders.of(getActivity()).get(NewOrderFragVM.class);

        ButterKnife.bind(this, mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!NetworkUtil.isConnected(getContext())){
            showAlert(R.string.no_connection_message);
            return;
        }

        api = BaseClient.getBaseClient().create(Api.class);
        loadData();


    }

    private void loadData(){
        getProducts();
        observeProductsLoading();
        observeProductsLoadingError();
        getCities();
        observeCitiesLoading();
        observecitiesLoadingError();
    }

    private void getProducts() {
        String token=SharedHelper.getKey(getActivity(), LoginActivity.TOKEN);
        newOrderFragVM.getProducts(token,null)
        .observe(this, new Observer<Response<AllProducts>>() {
                    @Override
                    public void onChanged(Response<AllProducts> response) {
                        //Log.d("NEWORDERFRAGG", "onChanged: "+(response.body().getCode()));

                        allProducts = response.body();
                        if (allProducts != null&&allProducts.getProducts()!=null) {
                            products = new ArrayList<>();
                            for (ProductData product : allProducts.getProducts()) {
                                products.add(new ProductForSpinner(product.getProduct_name(), product.getProduct_image(), product.getProduct_id(),product.getProduct_real_price()));
                            }
                            ArrayAdapter<String> myAdapter = new ProductSpinnerAdapter(
                                    getActivity(), products);
                            product.setAdapter(myAdapter);

                            product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position==0){
                                        productId=null;
                                        return;
                                    }

                                    productId = allProducts.getProducts().get(position-1).getProduct_id();
                                    // Toast.makeText(getActivity(), ""+productId, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }

                    }
                });
    }

    private void observeProductsLoading(){
        newOrderFragVM
                .getIsProductsLoading()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        //todo handle products loading
                        Log.d("NEWORDERFRAGG","is loading "+aBoolean);
                    }
                });
    }

    private void observeProductsLoadingError(){
        newOrderFragVM
                .getProductsLoadingError()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(getContext(), R.string.error_getting_products, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void getCities(){
        if (cities != null && cities.size() > 1) {
            cities.clear();
        }
        newOrderFragVM.getCities( SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                SharedHelper.getKey(getActivity(), LoginActivity.PARENT_ID))
                .observe(this, new Observer<Response<Cities>>() {
                    @Override
                    public void onChanged(Response<Cities> response) {
                        if (response.body()!=null&&response.body().getCities() != null) {
                            if (response.body().getCities().size() > 0) {

                                citiesBody = response.body().getCities();
                                for (Cities.City city : citiesBody) {
                                    cities.add(city.getCity_name()+" >> "+
                                            NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.valueOf(city.getShipping_cost()))+getActivity().getString(R.string.le)+" "+getString(R.string.for_shipping));
                                    citiesId.add(city.getCity_id());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(getActivity()
                                        , R.layout.layout_cities_spinner_item, cities);

                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                client_city.setAdapter(adapter);
                                client_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        CITY_ID = String.valueOf(citiesBody.get(position).getCity_id());
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                    }
                });
    }

    private void observeCitiesLoading(){
        newOrderFragVM
                .getIsCitiesLoading()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        //todo hable cities loading
                        Log.d("NEWORDERFRAGG","is loading "+aBoolean);
                    }
                });
    }

    private void observecitiesLoadingError(){
        newOrderFragVM
                .getCitiesLoadingError()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(getContext(), R.string.error_getting_products, Toast.LENGTH_SHORT).show();
                    }
                });
    }


  public static AllProducts allProducts;


    private ProgressDialog progressDialog;
    @OnClick(R.id.send_order)
    void sendOrder() {

        if (!NetworkUtil.isConnected(getContext())){
            showAlert(R.string.no_connection_message);
            return;
        }

        //todo add pssid and sender name to order
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جار ارسال الطلب");

        if (productId != null && CITY_ID != null &&
                !client_name.getText().toString().equals("")
                && !client_address.getText().toString().equals("")
                && !client_area.getText().toString().equals("")
                && !client_order_phone1.getText().toString().equals("")
                && !item_no.getText().toString().equals("")
                ) {
            int no;
            try {
                no=Integer.valueOf(item_no.getText().toString());
            }catch (Exception e){
                Toast.makeText(getContext(), R.string.enter_valid_quantity, Toast.LENGTH_SHORT).show();
                return;
            }

            orderItems.add(new OrderItem(productId,no));
            OrderPayload orderPayload=new OrderPayload(
                    SharedHelper.getKey(getActivity(),LoginActivity.TOKEN),
                    CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                    client_name.getText().toString(),
                    client_order_phone1.getText().toString(),
                    CITY_ID,
                    client_area.getText().toString(),
                    client_address.getText().toString(),
                    null,null,null,
                    orderItems
            );

            newOrderFragVM
                    .makeNewOrder(orderPayload)
                    .observe(this, new Observer<NewOrderResponse>() {
                        @Override
                        public void onChanged(NewOrderResponse response) {
                            if (response!=null&&response.getCode().equals("1411")){
                                showDialog(getString(R.string.invalid_ph_num));
                            }else if (response!=null&&response.getCode().equals("1200")){
                                Toast.makeText(getActivity(), getString(R.string.added_success), Toast.LENGTH_SHORT).show();
                                clearFields();
                                //     onButtonPressed();
                            }else {
                                if (response!=null)
                                showDialog(response.getDetails());
                            }
                        }
                    });
            observeIsOrderPlacing();
            observePlacingOrderError();

//
//            Api api = BaseClient.getBaseClient().create(Api.class);
//            api.recordNewOrder(
//                    SharedHelper.getKey(getActivity(),LoginActivity.TOKEN),
//                    CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
//                    productId,
//                    client_name.getText().toString(),
//                    client_order_phone1.getText().toString(),
//                    CITY_ID,
//                    client_area.getText().toString(),
//                    client_address.getText().toString(),
//                    item_no.getText().toString(),
//                    "0",null,null
//            ).enqueue(new Callback<NewOrderResponse>() {
//                @Override
//                public void onResponse(Call<NewOrderResponse> call, Response<NewOrderResponse> response) {
//                    progressDialog.dismiss();
//                    Log.d("GGGGGGGGGGgggggg", "onResponse: "+response.body().getCode());
//                    if (response.body().getCode().equals("1411")){
//                        showDialog(getString(R.string.invalid_ph_num));
//                    }else if (response.body().getCode().equals("1200")){
//                        Toast.makeText(getActivity(), getString(R.string.added_success), Toast.LENGTH_SHORT).show();
//                        clearFields();
//                   //     onButtonPressed();
//                    }else {
//                        showDialog(response.body().getDetails());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<NewOrderResponse> call, Throwable t) {
//                    progressDialog.dismiss();
//                    showAlert(R.string.error_placing_order);
//                    Log.d("GGGGGGGGGGgggggg", "onFailure: "+t.getMessage());
//                }
//            });
        }else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "برجاء ادخال جميع البيانات", Toast.LENGTH_SHORT).show();
        }
    }

    private void observeIsOrderPlacing(){
        newOrderFragVM
                .getIsOrderPlacing()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean b) {
                        if (b)
                          progressDialog.show();
                        else
                        progressDialog.dismiss();
                    }
                });
    }

    private void observePlacingOrderError(){
        newOrderFragVM
                .getOrderPlacingError()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        progressDialog.dismiss();
                        showAlert(R.string.error_placing_order);
                    }
                });
    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_warning);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showAlert(int textId){
        Alerter.create(getActivity())
                .setText(textId)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setBackgroundColorRes(R.color.material_red_600)
                .setDuration(4000)
                .enableSwipeToDismiss()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .show();
    }

    private void clearFields(){
        client_name.setText("");
        client_address.setText("");
        client_area.setText("");
        client_order_phone1.setText("");
        item_no.setText("");
        productId=null;
        product.setSelection(0);

    }
//
//    private SendOrderListener mListener;
//
//    public interface SendOrderListener {
//        // TODO: Update argument type and name
//        void orderSentGetNewOrder();
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed() {
//        if (mListener != null) {
//            mListener.orderSentGetNewOrder();
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof SendOrderListener) {
//            mListener = (SendOrderListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    };
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

}
