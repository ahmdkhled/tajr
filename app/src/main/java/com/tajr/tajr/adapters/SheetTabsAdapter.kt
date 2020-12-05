package com.tajr.tajr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tajr.tajr.R
import com.tajr.tajr.databinding.LayoutSheetTabBinding
import com.tajr.tajr.models.Sheet

class SheetTabsAdapter(var sheets:ArrayList<Sheet>) :RecyclerView.Adapter<SheetTabsAdapter.SheetTabVH>(){

    lateinit var onTabClickListener: OnTabClickListener
    constructor(sheets:ArrayList<Sheet>,onTabClickListener: OnTabClickListener):this(sheets){
        this.onTabClickListener=onTabClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetTabVH {
        val binding=DataBindingUtil.inflate<LayoutSheetTabBinding>(LayoutInflater.from(parent.context),
        R.layout.layout_sheet_tab,parent,false)
        return SheetTabVH(binding)
    }

    override fun getItemCount(): Int {
        return sheets.size
    }

    override fun onBindViewHolder(holder: SheetTabVH, position: Int) {
        holder.binding.name.text=sheets.get(position)
                .properties
                .title

        holder.binding.root
                .setOnClickListener {
                    onTabClickListener.onTabClicked(position)
                }
    }

    fun setSheet(sheets:ArrayList<Sheet>){
        this.sheets=sheets
        notifyDataSetChanged()
    }

    interface OnTabClickListener{
        fun onTabClicked(tabIndex:Int)
    }

    class SheetTabVH(var binding: LayoutSheetTabBinding) :RecyclerView.ViewHolder(binding.root){

    }
}