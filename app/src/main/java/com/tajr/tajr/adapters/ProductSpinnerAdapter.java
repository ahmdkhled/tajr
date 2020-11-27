package com.tajr.tajr.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tajr.tajr.R;
import com.tajr.tajr.models.ProductForSpinner;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductSpinnerAdapter extends ArrayAdapter<String > {

    private List<ProductForSpinner>products;
    private Context mContext;
    private static final int HEADER_TYPE=5;
    private static final int PRODUCT_TYPE=6;

    public ProductSpinnerAdapter(@NonNull Context context, List<ProductForSpinner>products) {
        super(context, R.layout.layout_product_spinner_item2);
        this.products = products;
        this.mContext = context;
    }



    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return products.size()+1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         ViewHolder mViewHolder = new ViewHolder();

        Log.d("SPINNNEER", position+"convertView  >>>>> "+(convertView==null ));

        LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (getItemViewType(position)==HEADER_TYPE){
                convertView = mInflater.inflate( R.layout.spinner_default_layout, parent, false);

            }else{
                convertView = mInflater.inflate( R.layout.layout_product_spinner_item2, parent, false);

                mViewHolder.mFlag =  convertView.findViewById(R.id.product_image);
                mViewHolder.mName =  convertView.findViewById(R.id.product_name);
                mViewHolder.price =  convertView.findViewById(R.id.price);

                convertView.setTag(mViewHolder);
            }

        if (getItemViewType(position)==PRODUCT_TYPE) {

            mViewHolder.mFlag.setImageURI(products.get(position - 1).getImage());
            mViewHolder.mName.setText(products.get(position - 1).getName());
            mViewHolder.price.setVisibility(View.VISIBLE);
            mViewHolder.price.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.valueOf(products.get(position - 1).getItemCost())) + mContext.getString(R.string.le));
       }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)return HEADER_TYPE;
        return PRODUCT_TYPE;
    }

    private static class ViewHolder {
        SimpleDraweeView mFlag;
        TextView mName;
        TextView price;

    }
}
