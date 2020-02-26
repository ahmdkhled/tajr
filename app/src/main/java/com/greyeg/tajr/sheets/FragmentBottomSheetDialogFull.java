package com.greyeg.tajr.sheets;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.greyeg.tajr.R;
import com.greyeg.tajr.adapters.ExtraDataAdapter2;
import com.greyeg.tajr.adapters.ProductAdapter;
import com.greyeg.tajr.helper.EndlessRecyclerViewScrollListener;
import com.greyeg.tajr.helper.ProductUtil;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.models.AllProducts;
import com.greyeg.tajr.models.OrderProduct;
import com.greyeg.tajr.models.Pages;
import com.greyeg.tajr.models.ProductData;
import com.greyeg.tajr.models.ProductExtra;
import com.greyeg.tajr.repository.ProductsRepo;
import com.greyeg.tajr.view.dialogs.ProductDetailDialog;
import com.greyeg.tajr.viewmodels.ProductDetailDialogVM;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentBottomSheetDialogFull extends BottomSheetDialogFragment implements ProductAdapter.OnProductClicked {

    private static final String TAG = "FragmentBottomSheetDial";
    private BottomSheetBehavior mBehavior;

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
    private ProductDetailDialog.OnProductUpdated onProductUpdated;
    private Pages pages;
    private String oldProductId;
    private ProductData currentProduct;
    private ProductDetailDialogVM productDetailDialogVM;
    private boolean isLoading=false;

    public FragmentBottomSheetDialogFull(OrderProduct product, ProductDetailDialog.OnProductUpdated onProductUpdated) {
        Log.d("DIALOOGG", "ProductDetailDialog: ");
        this.product = product;
        oldProductId=product.getId();
        this.onProductUpdated = onProductUpdated;
    }

    public FragmentBottomSheetDialogFull() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.product_detail_dialog, null);

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {

                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {

                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        productDetailDialogVM= ViewModelProviders.of(this).get(ProductDetailDialogVM.class);

        Log.d("DIALOOGG", "onCreateView: ");
        if (product!=null) productDetailDialogVM.setProduct(product);
        product=productDetailDialogVM.getProduct();
        if (onProductUpdated!=null)productDetailDialogVM.setOnProductUpdated(onProductUpdated);
        onProductUpdated=productDetailDialogVM.getOnProductUpdated();

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
        productPrice.setText(getString(R.string.product_price,product.getPrice()));

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
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
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
}
