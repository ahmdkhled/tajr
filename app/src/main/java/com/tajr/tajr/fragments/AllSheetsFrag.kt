package com.tajr.tajr.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.tajr.tajr.R
import com.tajr.tajr.databinding.FragAllSheetsBinding

class AllSheetsFrag :Fragment() {

    lateinit var binding: FragAllSheetsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.frag_all_sheets,container,false)

        return binding.root
    }
}