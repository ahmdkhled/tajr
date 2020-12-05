package com.tajr.tajr.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tajr.tajr.R
import com.tajr.tajr.databinding.LayoutCellBinding
import com.tajr.tajr.databinding.LayoutSheetRowBinding
import com.tajr.tajr.models.RowData

class SheetRowAdapter(var rows:ArrayList<RowData>)  :RecyclerView.Adapter<SheetRowAdapter.SheetRowVH>(){



    private  val TAG = "SheetRowAdapter"
    lateinit var context:Context
    private var header=false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetRowVH {
        context=parent.context
        val binding= DataBindingUtil.inflate<LayoutSheetRowBinding>(LayoutInflater.from(parent.context),
                R.layout.layout_sheet_row,parent,false)
        return SheetRowVH(binding)
    }

    override fun getItemCount(): Int {
        return rows.size
    }

    override fun onBindViewHolder(holder: SheetRowVH, position: Int) {
        val values=rows.get(position).values
        val cellAdapter=SheetCellAdapter(values)
        val layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        holder.binding.rowsRecycler.adapter=cellAdapter
        holder.binding.rowsRecycler.layoutManager=layoutManager

        val divider= DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        holder.binding.rowsRecycler.addItemDecoration(divider)
        header=true
    }

    fun setSheetRows(rows:ArrayList<RowData>){
        this.rows=rows
        notifyDataSetChanged()
    }

    class SheetRowVH(var binding: LayoutSheetRowBinding) :RecyclerView.ViewHolder(binding.root){

    }
}