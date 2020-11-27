package com.tajr.tajr.view.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tajr.tajr.R;
import com.tajr.tajr.adapters.ExtraDataAdapter2;
import com.tajr.tajr.adapters.ProductAdapter;
import com.tajr.tajr.helper.EndlessRecyclerViewScrollListener;
import com.tajr.tajr.helper.ExtraDataHelper;
import com.tajr.tajr.helper.ProductUtil;
import com.tajr.tajr.helper.SessionManager;
import com.tajr.tajr.helper.font.RobotoTextView;
import com.tajr.tajr.models.AllProducts;
import com.tajr.tajr.models.OrderProduct;
import com.tajr.tajr.models.Pages;
import com.tajr.tajr.models.ProductData;
import com.tajr.tajr.repository.ProductsRepo;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AddProductDialog extends DialogFragment implements ProductAdapter.OnProductClicked{
    @BindView(R.id.products_recycler)
    RecyclerView productsRecycler;
    @BindView(R.id.extra_data_recycler)
    RecyclerView extra_data_recycler;
    @BindView(R.id.add_product)
    RobotoTextView addProduct;
    @BindView(R.id.product_no)
    EditText items_no;
    @BindView(R.id.products_PB)
    ProgressBar productsPB;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.product_price)
    TextView productPrice;
    @BindView(R.id.productImg)
    ImageView productImage;
    private ProductAdapter productAdapter;
    private boolean isLoading=false;
    private Pages pages;
    private ExtraDataAdapter2 extraDataAdapter;
    private ProductData selectedProduct;
    private OnProductAdded onProductAdded;


    public AddProductDialog(OnProductAdded onProductAdded) {
        this.onProductAdded = onProductAdded;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialog=inflater.inflate(R.layout.layout_add_poduct_dialog,container,false);
        ButterKnife.bind(this,dialog);

        getProducts("1");
        productAdapter=new ProductAdapter(getContext(),null, this);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getContext());
        productsRecycler.setLayoutManager(layoutManager);
        productsRecycler.setAdapter(productAdapter);
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

    @OnClick(R.id.add_product)
    void addProduct(){
        int items;
            try {
                items=Integer.valueOf(items_no.getText().toString());
                if (items==0){
                    Toast.makeText(getContext(),R.string.zero_quantity , Toast.LENGTH_SHORT).show();
                    return;
                }
            }catch (Exception e){
                Toast.makeText(getContext(), R.string.enter_valid_quantity, Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String,Object> values =ExtraDataHelper.getValues(getContext(),extraDataAdapter,extra_data_recycler);
            if (values==null) {
                Toast.makeText(getContext(), R.string.complete_fields_error, Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedProduct!=null){
                OrderProduct orderProduct=ProductUtil.toOrderProduct(selectedProduct,new OrderProduct(items));
                ProductUtil.setExtraDataValues(orderProduct, values);
                onProductAdded.onProductAdded(orderProduct);
            }



    }



    private void getProducts(String page){
        if (page==null||page.equals("1"))
            productsPB.setVisibility(View.VISIBLE);
        else
            productAdapter.setLoadingView(page);

        Log.d("PAGINATIONN","getProducts "+page);
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
    public void onProductClicked(ProductData product) {
        selectedProduct=product;
        productName.setText(product.getProduct_name());
        Picasso.get()
                .load(product.getProduct_image())
                .into(productImage);
        productPrice.setText(String.valueOf(product.getProduct_real_price()));
        if (product.getExtra_data().isEmpty()) {
            extra_data_recycler.setVisibility(View.GONE);
        } else {
            extra_data_recycler.setVisibility(View.VISIBLE);
        }

        extraDataAdapter=new ExtraDataAdapter2(getContext(),product.getExtra_data());
        extra_data_recycler.setAdapter(extraDataAdapter);
        extra_data_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    public interface OnProductAdded{
        void onProductAdded(OrderProduct product);
    }
}
