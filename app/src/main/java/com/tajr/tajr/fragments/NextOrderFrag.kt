package com.tajr.tajr.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.tajr.tajr.R
import com.tajr.tajr.databinding.FragmentNextOrderBinding

class NextOrderFrag :Fragment() {

    lateinit var binding:FragmentNextOrderBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_next_order,container,false)

        return binding.root
    }
}