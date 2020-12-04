package com.tajr.tajr.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.tajr.tajr.R
import com.tajr.tajr.activities.MainActivity
import com.tajr.tajr.adapters.SpreadSheetsAdapter
import com.tajr.tajr.databinding.FragAllSheetsBinding
import com.tajr.tajr.repository.DriveRepo

class AllSheetsFrag :Fragment(),SpreadSheetsAdapter.OnSpreadSheetClickListener {

    lateinit var binding: FragAllSheetsBinding
    lateinit var spreadSheetsAdapter: SpreadSheetsAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.frag_all_sheets,container,false)
        binding.spreadsheetsRecycler.layoutManager=GridLayoutManager(context,2)
        DriveRepo().getAllSpreadSheets()
                .observe(this, Observer {
                    spreadSheetsAdapter=SpreadSheetsAdapter(it,this)
                    binding.spreadsheetsRecycler.adapter=spreadSheetsAdapter

                })

        return binding.root
    }

    override fun onSpreadSheetClickListener(spreadSheetId: String) {
        val spreadSheetFrag=SpreadSheetFrag()
        val b=Bundle()
        b.putString(spreadSheetFrag.SPREADSHEET_ID_KEY,spreadSheetId)
        spreadSheetFrag.arguments=b
        (activity as MainActivity).addFrag(spreadSheetFrag)
    }
}