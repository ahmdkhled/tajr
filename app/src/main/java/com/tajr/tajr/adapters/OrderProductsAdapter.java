package com.tajr.tajr.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajr.tajr.R;
import com.tajr.tajr.models.OrderProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.CartItemHolder>{

    private Context context;
    private ArrayList<OrderProduct> products;
    private OnProductItemEvent onProductItemEvent;

    public OrderProductsAdapter(Context context, ArrayList<OrderProduct> products, OnProductItemEvent onProductItemEvent) {
        Log.d("UPDATEE", "OrderProductsAdapter construct: "+products.size()+" --->" +products.get(0).toString());
        this.context = context;
        this.products = products;
        this.onProductItemEvent = onProductItemEvent;
    }

    @NonNull
    @Override
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        return new CartItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder holder, int position) {
        OrderProduct product=products.get(position);
        Log.d("onProductAdded","id --->  "+product.getId());
        holder.name.setText(product.getName());
        holder.quantity.setText(String.valueOf(product.getItems_no()));

        Picasso.get()
                .load(product.getImage())
                .into(holder.thumbnail);
    }

    public void updateProduct(String productId,OrderProduct product){
        Log.d("UPDATEE",productId+" /**/* ");
        int index =products.indexOf(new OrderProduct(productId));
        //Log.d("UPDATEE", index+product.getName()+" updateProduct: " +" "+products.get(0).toString());
        if (index==-1)return;
        products.set(index,product);
        notifyDataSetChanged();
    }
    public boolean addProduct(OrderProduct product){
        Log.d("onProductAdded", product.getId()+" onProductAdded: "+this.products.indexOf(new OrderProduct(product.getId())));

        if (this.products.indexOf(new OrderProduct(product.getId()))==-1){
            products.add(product);
            notifyDataSetChanged();
            return true;
        }

        return false;
    }

    @Override
    public int getItemCount() {
        return products==null?0:products.size();
    }

    public ArrayList<OrderProduct> getProducts() {
        return products;
    }

    public int getOrderTotal(){
        int total=0;
        for (OrderProduct product:this.products) {
            total+=product.getPrice()*product.getItems_no();
        }
        return  total;
    }

    class CartItemHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_img)
        ImageView thumbnail;
        @BindView(R.id.increase)
        ImageView increaseQuantity;
        @BindView(R.id.decrease)
        ImageView decreaseQuantity;
        @BindView(R.id.delete)
        ImageView delete;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.name)
        TextView name;

        CartItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(view -> {
                OrderProduct product=products.get(getAdapterPosition());
                Log.d("UPDATEE", "CartItemHolder: "+product.getImage());
                onProductItemEvent.OnProductItemClicked(
                        new OrderProduct(product.getId(),product.getName(),product.getPrice()
                        ,product.getItems_no(),product.getImage(),product.getCost(),product.getExtras()));
            });

            delete.setOnClickListener(view -> {
                products.remove(getAdapterPosition());
                onProductItemEvent.onCartItemsChange();
                notifyDataSetChanged();
            });


            increaseQuantity.setOnClickListener(view -> {
                OrderProduct product=products.get(getAdapterPosition());
                int quantity=product.getItems_no();
                quantity++;
                product.setItems_no(quantity);
                onProductItemEvent.onCartItemsChange();
                notifyDataSetChanged();

            });

            decreaseQuantity.setOnClickListener(view -> {
                OrderProduct product=products.get(getAdapterPosition());
                int quantity=product.getItems_no();
                if (quantity==1)return;
                quantity--;
                product.setItems_no(quantity);
                onProductItemEvent.onCartItemsChange();

                notifyDataSetChanged();
            });
        }
    }

    public interface OnProductItemEvent{
        void onCartItemsChange();
        void OnProductItemClicked(OrderProduct product);
    }
}


