package com.tajr.tajr.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tajr.tajr.R;
import com.tajr.tajr.models.Subscriber;

import java.util.ArrayList;

public class SubscribersAdapter extends RecyclerView.Adapter<SubscribersAdapter.SubscriberHolder> {

    Context context;
    private ArrayList<Subscriber> subscribers;
    private OnSubscriberSelected onSubscriberSelected;



    public SubscribersAdapter(Context context, ArrayList<Subscriber> subscribers, OnSubscriberSelected onSubscriberSelected) {
        this.context = context;
        this.subscribers = subscribers;
        this.onSubscriberSelected = onSubscriberSelected;
    }

    @NonNull
    @Override
    public SubscriberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context)
                .inflate(R.layout.subscriber_row,parent,false);
        return new SubscriberHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriberHolder holder, int position) {
        Subscriber subscriber=subscribers.get(position);
        holder.name.setText(subscriber.getName());

        String image_base=subscriber.getImg_base();
        //showImage(image_base,holder.image);
        Log.d("IMGBASSEE", image_base.substring(22));
        showImage(image_base.substring(22),holder.image);



    }

    @Override
    public int getItemCount() {
        return subscribers==null?0:subscribers.size();
    }

    class SubscriberHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView image;
        public SubscriberHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.subscriberName);
            image=itemView.findViewById(R.id.subscriberImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSubscriberSelected.onSubscriberSelected(
                            subscribers.get(getAdapterPosition()).getPsid()
                    ,subscribers.get(getAdapterPosition())
                                    .getId());
                }
            });
        }
    }

    private void showImage(String img_base,ImageView img){
        byte[] imageBytes=Base64.decode(img_base,Base64.DEFAULT);
        Bitmap decodedImg= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
        img.setImageBitmap(decodedImg);
    }

    public interface OnSubscriberSelected{
        void onSubscriberSelected(String psid,String userId);
    }
}
