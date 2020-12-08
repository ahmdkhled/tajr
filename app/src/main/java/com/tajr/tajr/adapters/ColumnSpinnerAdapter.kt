package com.tajr.tajr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.tajr.tajr.R
import com.tajr.tajr.databinding.LayoutColumnSpinnerBinding

class ColumnSpinnerAdapter(var columns:ArrayList<String>) :BaseAdapter() {

    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        val binding=DataBindingUtil.inflate<LayoutColumnSpinnerBinding>(LayoutInflater.from(parent?.context),
                R.layout.layout_column_spinner,parent,false )
        binding.name.text=columns[position]
        return binding.root
    }

    override fun getItem(p0: Int): Any {
        return columns[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return columns.size
    }


}