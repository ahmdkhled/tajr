package com.greyeg.tajr.view.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.adapters.ExtraDataAdapter2;
import com.greyeg.tajr.adapters.ProductAdapter;
import com.greyeg.tajr.helper.EndlessRecyclerViewScrollListener;
import com.greyeg.tajr.helper.ProductUtil;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.models.AllProducts;
import com.greyeg.tajr.models.OrderProduct;
import com.greyeg.tajr.models.Pages;
import com.greyeg.tajr.models.ProductData;
import com.greyeg.tajr.models.ProductExtra;
import com.greyeg.tajr.repository.ProductsRepo;
import com.greyeg.tajr.viewmodels.ProductDetailDialogVM;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProductDetailDialog extends DialogFragment implements ProductAdapter.OnProductClicked {

    @BindView(R.id.productsRecycler)
    RecyclerView productsRecycler;
    @BindView(R.id.extra_data_recycler)
    RecyclerView extraDataRecycler;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.product_price)
    TextView productPrice;
    @BindView(R.id.productImg)
    ImageView productImage;
    @BindView(R.id.quantity)
    TextView quantity;
    @BindView(R.id.increase)
    ImageView increment;
    @BindView(R.id.decrease)
    ImageView decrement;
    @BindView(R.id.products_PB)
    ProgressBar productsPB;


    private OrderProduct product;
    private ExtraDataAdapter2 extraDataAdapter;
    private ProductAdapter productAdapter;
    private OnProductUpdated onProductUpdated;
    private Pages pages;
    private String oldProductId;
    private ProductData currentProduct;
    private ProductDetailDialogVM productDetailDialogVM;
    private boolean isLoading=false;


    public ProductDetailDialog(OrderProduct product, OnProductUpdated onProductUpdated) {
        Log.d("DIALOOGG", "ProductDetailDialog: ");
        this.product = product;
        oldProductId=product.getId();
        this.onProductUpdated = onProductUpdated;
    }

    public ProductDetailDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        productDetailDialogVM= ViewModelProviders.of(this).get(ProductDetailDialogVM.class);

        Log.d("DIALOOGG", "onCreateView: ");
        if (product!=null) productDetailDialogVM.setProduct(product);
         product=productDetailDialogVM.getProduct();
        if (onProductUpdated!=null)productDetailDialogVM.setOnProductUpdated(onProductUpdated);
        onProductUpdated=productDetailDialogVM.getOnProductUpdated();

        View dialog=inflater.inflate(R.layout.product_detail_dialog,container,false);
        ButterKnife.bind(this,dialog);
        product=productDetailDialogVM.getProduct();
        onProductUpdated=productDetailDialogVM.getOnProductUpdated();
        populateProductDetail(product);
        getProducts(null);

        productAdapter=new ProductAdapter(getContext(),null,this);
        productsRecycler.setAdapter(productAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        productsRecycler.setLayoutManager(layoutManager);
        productsRecycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        productsRecycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager,2) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isLoading)return;
                isLoading=true;
                page++;
                Log.d("PAGINATIONN", "onLoadMore: "+page);
                if (!pages.exceedLimit(page))
                    getProducts(String.valueOf(page));

            }
        });

        return dialog;

    }

    @OnClick(R.id.updateProduct)
    public void updateProduct(){
        HashMap<String,Object> values =getExtraDataValues();
        if (values==null)return;
        if (currentProduct!=null)
        product=ProductUtil.toOrderProduct(currentProduct, product);
        for (String value:values.keySet()) {
            int position=product.getExtras().indexOf(new ProductExtra(value));
            product.getExtras().get(position).setValue(value);
        }
        ProductUtil.setExtraDataValues(product,values);
        Log.d("EXTRAAAAAAA", "updateProduct: ");
        onProductUpdated.onProductUpdated(product,oldProductId);
        dismiss();
    }


    private HashMap<String, Object> getExtraDataValues(){
        HashMap<String,Object> values=new HashMap<>();

        ArrayList<ProductExtra> extraData=extraDataAdapter.getExtraData();

        for (int i = 0; i < extraDataAdapter.getItemCount(); i++) {



            View view = extraDataRecycler.getChildAt(i);
            if (Boolean.valueOf(extraData.get(i).Is_list())){
                Spinner spinner=view.findViewById(R.id.spinnerValue);

                if (Boolean.valueOf(extraData.get(i).getRequired())&&spinner.getSelectedItemPosition()==0){
                    Toast.makeText(getContext(), R.string.complete_fields_error, Toast.LENGTH_SHORT).show();
                    return null;
                }

                if (spinner.getSelectedItemPosition()>0){
                    String value=extraData.get(i).getList().get(spinner.getSelectedItemPosition()-1);
                    values.put(extraData.get(i).getHtml(),value);
                }

            }else {

                EditText editText=view.findViewById(R.id.value);
                String value=editText.getText().toString();
                if (Boolean.valueOf(extraData.get(i).getRequired())&& TextUtils.isEmpty(value)){
                    Toast.makeText(getContext(), R.string.complete_fields_error, Toast.LENGTH_SHORT).show();
                    return null;
                }
                values.put(extraData.get(i).getHtml(),value);
            }

        }

        Log.d("EXTRAAAAAAA", "getExtraDataValues: "+values.toString());
        return values;
    }

    private void populateProductDetail(OrderProduct product){
        productName.setText(product.getName());
        Picasso.get()
                .load(product.getImage())
                .into(productImage);

        if (product.getItems_no()==0)
        quantity.setText(String.valueOf(1));
        else
        quantity.setText(String.valueOf(product.getItems_no()));
        productPrice.setText(String.valueOf(product.getPrice()));

        increment.setOnClickListener(view -> {
            if (TextUtils.isEmpty(quantity.getText().toString()))return;
            int q= Integer.parseInt(quantity.getText().toString());
            q++;
            quantity.setText(String.valueOf(q));
            product.setItems_no(q);
        });
        decrement.setOnClickListener(view -> {
            if (TextUtils.isEmpty(quantity.getText().toString()))return;

            int q= Integer.parseInt(quantity.getText().toString());
            if (q==1)return;
            q--;
            quantity.setText(String.valueOf(q));
            product.setItems_no(q);


        });


        Log.d("EXTRAA", "populateProductDetail: "+product.getExtras().size());
        extraDataAdapter=new ExtraDataAdapter2(getContext(),product.getExtras());
        extraDataRecycler.setAdapter(extraDataAdapter);
        extraDataRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getProducts(String page){
        if (page==null||page.equals("1"))
            productsPB.setVisibility(View.VISIBLE);
        else
            productAdapter.setLoadingView(page);

        //Log.d("PAGINATIONN","getProducts "+page);
        String token= SessionManager.getInstance(getContext()).getToken();
        ProductsRepo
                .getInstance()
                .getProducts(token,null,page,"10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<AllProducts>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<AllProducts> response) {
                        productsPB.setVisibility(View.GONE);
                        AllProducts products=response.body();
                        if (response.isSuccessful()&&products!=null){
//                            Log.d("PAGINATIONN", products.getPages().getCurrent()
//                                    +" of: "+products.getPages().getOf());
                            pages=products.getPages();
                            //if (page==null||page.equals("1"))
                            productAdapter.addProducts(products.getProducts());


                        }else{
                            //todo handle case of no products
                            Toast.makeText(getContext(), R.string.error_getting_products, Toast.LENGTH_SHORT).show();
                        }
                        isLoading=false;

                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading=false;
                        productsPB.setVisibility(View.GONE);
                        Toast.makeText(getContext(), R.string.error_getting_products, Toast.LENGTH_SHORT).show();

                    }
                });
    }



    @Override
    public void onResume() {
        super.onResume();
        if (getDialog()!=null&&getDialog().getWindow()!=null)
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onProductClicked(ProductData product) {
        currentProduct=product;
        if (product.getProduct_id().equals(this.product.getId())){
            Log.d("UPDATEEEE", "main Product: ");
            populateProductDetail(this.product);
        }else
        populateProductDetail(ProductUtil.toOrderProduct(currentProduct,new OrderProduct()));


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("DIALOOGG", "onAttach: ");

    }

    public interface OnProductUpdated{
        void onProductUpdated(OrderProduct product,String productId);
    }
}
