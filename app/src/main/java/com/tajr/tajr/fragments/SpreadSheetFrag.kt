package com.tajr.tajr.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.tajr.tajr.R
import com.tajr.tajr.databinding.FragSpreadsheetBinding
import com.tajr.tajr.models.SpreadSheetRes
import com.tajr.tajr.server.BaseClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SpreadSheetFrag :Fragment() {

    lateinit var binding: FragSpreadsheetBinding
    val SPREADSHEET_ID_KEY="sheet_id"
    private val TAG = "SpreadSheetFrag"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.frag_spreadsheet,container,false)

        val b=arguments
        val spreadsheetId=b?.getString(SPREADSHEET_ID_KEY)
        return binding.root
    }
    

}