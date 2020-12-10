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
import com.tajr.tajr.R
import com.tajr.tajr.databinding.FragmentNextOrderBinding
import com.tajr.tajr.helper.SharedHelper
import com.tajr.tajr.models.Value
import com.tajr.tajr.viewmodels.NextOrderFragVM

class NextOrderFrag :Fragment() {

    lateinit var binding:FragmentNextOrderBinding
    lateinit var nextOrderFragVM:NextOrderFragVM
    private  val TAG = "NextOrderFrag"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_next_order,container,false)
        nextOrderFragVM=ViewModelProvider(this).get(NextOrderFragVM::class.java)

        getNextOrder()


        return binding.root
    }

    private fun getNextOrder(){
        val sheet_id=SharedHelper.getKey(context,"sheet_id")
        val order_status_index=SharedHelper.getIntegerValue(context,"order_status_index")
        val tab_index=SharedHelper.getIntegerValue(context,"tab_index")

        Log.d(TAG, "getNextOrder: $order_status_index")
        nextOrderFragVM.getSpreadSheetData(sheet_id)
                .observe(viewLifecycleOwner, Observer {res->
                    val rows = res.model?.sheets?.get(tab_index)?.data?.get(0)?.rowData
                    Log.d(TAG, "getNextOrder: $rows")
                    if (rows != null) {
                        var i=0;
                        for (row in rows){
                            val columns=row.values
                            val order_status= columns?.get(order_status_index)?.formattedValue
                            if (order_status.equals("waiting_confirmtion")){
                                populateOrder(row.values)
                                break
                            }
                            i++

                        }
                    }


                })
    }

    private fun populateOrder(rows: ArrayList<Value>?) {
        Log.d(TAG, "populateOrder: $rows")
        if (rows==null){
            return
        }

        val nameIndex=SharedHelper.getIntegerValue(context,"name_index")
        val mobileIndex=SharedHelper.getIntegerValue(context,"mobile_index")
        val addressIndex=SharedHelper.getIntegerValue(context,"address_index")
        val order_status_index=SharedHelper.getIntegerValue(context,"order_status_index")


        Log.d(TAG, "populateOrder: $mobileIndex")



        if (nameIndex>-1) binding.clientName.setText(rows[nameIndex].formattedValue.toString())
        if (mobileIndex>-1) binding.mobileNum.setText(rows[mobileIndex].formattedValue.toString())
        if (addressIndex>-1) binding.clientAddress.setText(rows[addressIndex].formattedValue.toString())
        if (order_status_index>-1) binding.orderStatus.setText(rows[order_status_index].formattedValue.toString())



    }
}