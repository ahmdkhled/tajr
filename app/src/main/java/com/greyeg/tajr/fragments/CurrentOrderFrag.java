package com.greyeg.tajr.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.greyeg.tajr.R;
import com.greyeg.tajr.databinding.CurrentOrderFragBinding;

public class CurrentOrderFrag extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CurrentOrderFragBinding binding= DataBindingUtil
                .inflate(LayoutInflater.from(getContext()),R.layout.current_order_frag,container,false);
        return binding.getRoot();
    }
}
