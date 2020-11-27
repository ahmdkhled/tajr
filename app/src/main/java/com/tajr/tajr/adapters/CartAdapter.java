package com.tajr.tajr.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tajr.tajr.R;
import com.tajr.tajr.models.CartItem;
import com.tajr.tajr.models.ProductData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemHolder> {

    private Context context;
    private ArrayList<CartItem> cartItems;
    private OnCartItemEvent onCartItemEvent;
    private boolean isCartItem;


    public CartAdapter(Context context, OnCartItemEvent onCartItemEvent) {
        this.context = context;
        this.onCartItemEvent = onCartItemEvent;
    }

    public CartAdapter(Context context, OnCartItemEvent onCartItemEvent, boolean isCartItem) {
        this.context = context;
        this.onCartItemEvent = onCartItemEvent;
        this.isCartItem = isCartItem;
    }

    @NonNull
    @Override
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        if (isCartItem){


            ConstraintLayout.LayoutParams layoutParams=new ConstraintLayout.LayoutParams(250,250);
            //layoutParams.setMargins(5,0,5,0);
            v.findViewById(R.id.item_img).setLayoutParams(layoutParams);
        }
        return new CartItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder holder, int position) {
        // todo show error image

        ProductData product=cartItems.get(position).getProduct();
        Picasso.get()
                .load(product.getProduct_image())
                .into(holder.thumbnail);

        holder.quantity.setText(String.valueOf(cartItems.get(position).getQuantity()));
        holder.name.setText(cartItems.get(position).getProduct().getProduct_name());
    }

    @Override
    public int getItemCount() {
        return cartItems==null?0:cartItems.size();
    }

    public void addCartItem(CartItem cartItem){
        if (cartItems==null) cartItems=new ArrayList<>();
        this.cartItems.add(cartItem);
        Log.d("REPLCAEEE", " addCartItem: "+cartItem.getProduct().getProduct_id());
        notifyDataSetChanged();
    }

    public void replaceCartItem(String product_id,CartItem cartItem){
        int index=this.cartItems.indexOf(new CartItem(product_id));
        Log.d("REPLCAEEE", product_id+" replaceCartItem: "+cartItem.getProduct().getProduct_id());
        if (index>-1)
        this.cartItems.set(index,cartItem);
        else
            Log.d("REPLCAEEE", "error not found this product : "+product_id+" cart current "+cartItems.get(0).getProduct().getProduct_id());
        notifyDataSetChanged();
    }

    public void updateQuantity(String product_id,int quantity){
        this.cartItems.get(cartItems.indexOf(new CartItem(product_id))).setQuantity(quantity);
        notifyDataSetChanged();
    }

    public void emptyCart(){
        this.cartItems.clear();
        notifyDataSetChanged();
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

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int product_id=Integer.valueOf(cartItems.get(getAdapterPosition()).getProduct().getProduct_id());
                    cartItems.remove(cartItems.get(getAdapterPosition()));
                    notifyDataSetChanged();
                    onCartItemEvent.onCartItemDeleted(product_id);
                }
            });


            increaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartItem cartItem=cartItems.get(getAdapterPosition());
                    int product_id=Integer.valueOf(cartItem.getProduct().getProduct_id());
                    cartItem.setQuantity(cartItem.getQuantity()+1);
                    notifyDataSetChanged();
                    onCartItemEvent.onCartItemQuantityIncrease(product_id,cartItem.getQuantity());
                }
            });

            decreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartItem cartItem=cartItems.get(getAdapterPosition());
                    if (cartItem.getQuantity()==1) return;
                    int product_id=Integer.valueOf(cartItem.getProduct().getProduct_id());
                    cartItem.setQuantity(cartItem.getQuantity()-1);
                    notifyDataSetChanged();
                    onCartItemEvent.onCartItemQuantityDecrease(product_id,cartItem.getQuantity());
                }
            });
        }
    }

    public interface OnCartItemEvent{
        void onCartItemDeleted(int productId);
        void onCartItemQuantityIncrease(int productId,int quantity);
        void onCartItemQuantityDecrease(int productId,int quantity);
    }
}
