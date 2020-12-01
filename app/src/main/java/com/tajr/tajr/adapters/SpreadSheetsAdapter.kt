package com.tajr.tajr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tajr.tajr.R
import com.tajr.tajr.databinding.LayoutDriveFileBinding
import com.tajr.tajr.models.File

class SpreadSheetsAdapter(var spreadSheets :ArrayList<File>) :RecyclerView.Adapter<SpreadSheetsAdapter.SpreadSheetVH>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpreadSheetVH {
        val binding=DataBindingUtil.inflate<LayoutDriveFileBinding>(LayoutInflater.from(parent.context), R.layout.layout_drive_file,parent,false)
        return SpreadSheetVH(binding)
    }

    override fun onBindViewHolder(holder: SpreadSheetVH, position: Int) {
        holder.binding.name.text=spreadSheets.get(position).name
    }

    override fun getItemCount(): Int {
        return spreadSheets.size
    }

    class SpreadSheetVH(var binding: LayoutDriveFileBinding) :RecyclerView.ViewHolder(binding.root){

    }
}
