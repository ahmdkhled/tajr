package com.tajr.tajr.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tajr.tajr.R
import com.tajr.tajr.adapters.SheetRowAdapter
import com.tajr.tajr.adapters.SheetTabsAdapter
import com.tajr.tajr.databinding.FragSpreadsheetBinding
import com.tajr.tajr.models.Sheet
import com.tajr.tajr.models.SpreadSheetRes
import com.tajr.tajr.server.BaseClient
import com.tajr.tajr.viewmodels.SpreadSheetFragVM
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SpreadSheetFrag :Fragment(),SheetTabsAdapter.OnTabClickListener {

    lateinit var binding: FragSpreadsheetBinding
    lateinit var spreadSheetFragVM: SpreadSheetFragVM
    lateinit var tabsAdapter:SheetTabsAdapter
    lateinit var rowsAdapter:SheetRowAdapter
     var sheets=ArrayList<Sheet>()
    val SPREADSHEET_ID_KEY="sheet_id"
    private val TAG = "SpreadSheetFrag"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.frag_spreadsheet,container,false)
        spreadSheetFragVM= ViewModelProvider(this).get(SpreadSheetFragVM::class.java)
        val b=arguments
        val spreadsheetId=b?.getString(SPREADSHEET_ID_KEY)

        tabsAdapter= SheetTabsAdapter(ArrayList(),this)
        rowsAdapter= SheetRowAdapter(ArrayList())
        val layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val rowsLayoutManager=LinearLayoutManager(context)
        binding.tabsRecycler.adapter=tabsAdapter
        binding.tabsRecycler.layoutManager=layoutManager
        binding.sheetRecycler.adapter=rowsAdapter
        binding.sheetRecycler.layoutManager=rowsLayoutManager

        val divider=DividerItemDecoration(context,DividerItemDecoration.VERTICAL)
        binding.sheetRecycler.addItemDecoration(divider)

        getSpreadSheetData(spreadsheetId)
        return binding.root
    }

    fun getSpreadSheetData(spreadsheetId:String?){
        binding.progress.visibility=View.VISIBLE
        spreadSheetFragVM
                .getSpreadSheetData(spreadsheetId)
                .observe(viewLifecycleOwner, Observer {res->
                    if (res.error==null){
                        val sheets=res.model?.sheets
                        if (sheets!=null){
                            this.sheets=sheets
                            tabsAdapter.setSheet(sheets)
                            rowsAdapter.setSheetRows(sheets.get(0).data.get(0).rowData)
                            binding.progress.visibility=View.GONE

                        }
                    }
                })

    }

    override fun onTabClicked(tabIndex: Int) {
       rowsAdapter.setSheetRows(sheets.get(tabIndex).data.get(0).rowData)
    }


}