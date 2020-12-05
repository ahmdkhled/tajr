package com.tajr.tajr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tajr.tajr.R
import com.tajr.tajr.databinding.LayoutCellBinding
import com.tajr.tajr.models.Value

class SheetCellAdapter(var values:ArrayList<Value>) :RecyclerView.Adapter<SheetCellAdapter.SheetCellVH>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetCellVH {
        val binding=DataBindingUtil.inflate<LayoutCellBinding>(LayoutInflater.from(parent.context),
        R.layout.layout_cell,parent,false)
        return SheetCellVH(binding)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: SheetCellVH, position: Int) {
        val value=values.get(position).formattedValue
        if (value!=null)holder.binding.value.text=value
    }

    class  SheetCellVH(var binding: LayoutCellBinding):RecyclerView.ViewHolder(binding.root){

    }
}