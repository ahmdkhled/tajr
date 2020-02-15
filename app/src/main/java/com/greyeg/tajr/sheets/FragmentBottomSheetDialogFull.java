package com.greyeg.tajr.sheets;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.models.AllProducts;
import com.greyeg.tajr.models.ProductData;
import com.greyeg.tajr.models.CurrentOrderData;
import com.greyeg.tajr.models.CurrentOrderResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBottomSheetDialogFull extends BottomSheetDialogFragment {

    private static final String TAG = "FragmentBottomSheetDial";
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    private SimpleDraweeView product_image;
    private CurrentOrderResponse orderResponse;
    private TextView name,description,product_info,price,category_name;
    private ProgressBar progressBar;
    private View parent;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_bottom_sheet_dialog_full, null);

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        app_bar_layout = view.findViewById(R.id.app_bar_layout);

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

        view.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        initViews(view);
        fillData();
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void fillData() {
        orderResponse = CurrentOrderData.getInstance().getCurrentOrderResponse();
        getProducts();
    }

    public void getProducts() {
        BaseClient.getBaseClient().create(Api.class).getProducts(SessionManager.getInstance(getContext()).getToken(),
                orderResponse.getUserId())
                .enqueue(new Callback<AllProducts>() {
                    @Override
                    public void onResponse(Call<AllProducts> call, Response<AllProducts> response) {
                        System.out.println(response.body().getCode());
                        Log.d(TAG, "onResponse: " + response.body().toString());

                        for (ProductData data :response.body().getProducts() ){
                            if (data.getProduct_id()    .equals(orderResponse.getOrder().getProductId())){
                                product_image.setImageURI(data.getProduct_image());
                                name.setText(data.getProduct_name());
                                price.setText(data.getProduct_real_price());
                                description.setText(data.getProduct_describtion());
                                category_name.setText(data.getSub_category_name());
                                product_info.setText(data.getProduct_info());
                                progressBar.setVisibility(View.GONE);
                                parent.setVisibility(View.VISIBLE);

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<AllProducts> call, Throwable t) {
                        System.out.println(t.getMessage());
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    private void initViews(View view) {
        product_image = view.findViewById(R.id.product_image);
        name = view.findViewById(R.id.name);
        price = view.findViewById(R.id.price);
        description = view.findViewById(R.id.description);
        category_name = view.findViewById(R.id.category_name);
        product_info = view.findViewById(R.id.product_info);
        parent = view.findViewById(R.id.lyt_parent);
        progressBar = view.findViewById(R.id.progress_bar);
        parent.setVisibility(View.GONE);
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
}
