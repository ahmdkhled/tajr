package com.tajr.tajr.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tajr.tajr.R;
import com.tajr.tajr.models.SingleOrderProductsResponse;

public class SignleOrderProductsAdapter extends ArrayAdapter<String> {

    private SingleOrderProductsResponse products;

    private Context mContext;

    public SignleOrderProductsAdapter(@NonNull Context context, SingleOrderProductsResponse products) {
        super(context, R.layout.layout_product_spinner_item);
        this.products = products;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return products.getProducts().size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layout_product_spinner_item, parent, false);
            mViewHolder.mFlag = convertView.findViewById(R.id.product_image);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.product_name);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageURI(products.getProducts().get(position).getProductImage());
        mViewHolder.mName.setText(products.getProducts().get(position).getProductName());


        return convertView;
    }

    private static class ViewHolder {
        SimpleDraweeView mFlag;
        TextView mName;

    }
}
