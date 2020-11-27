package com.tajr.tajr.helper;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class Binder {

    Context context;

    public Binder(Context context) {
        this.context = context;
    }

    @BindingAdapter("android:src")
    public static void bindImage(ImageView imageView,String url){
        Log.d("binder", "bindImage: ");
        Picasso
                .get()
                .load(url)
                .into(imageView);
    }
}
