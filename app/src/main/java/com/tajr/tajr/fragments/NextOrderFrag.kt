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
import com.tajr.tajr.helper.CallHelper
import com.tajr.tajr.helper.SharedHelper
import com.tajr.tajr.models.Value
import com.tajr.tajr.view.dialogs.ProgressDialog
import com.tajr.tajr.viewmodels.NextOrderFragVM
import es.dmoral.toasty.Toasty

class NextOrderFrag :Fragment() {

    lateinit var binding:FragmentNextOrderBinding
    lateinit var nextOrderFragVM:NextOrderFragVM
    private var tabId:String? = null
    var mobileNumber:String?=null
    var i=-1;

    val progressDialog = ProgressDialog()
    private  val TAG = "NextOrderFrag"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_next_order,container,false)
        nextOrderFragVM=ViewModelProvider(this).get(NextOrderFragVM::class.java)

        getNextOrder()

        binding.call.setOnClickListener {
            if (mobileNumber==null){
                return@setOnClickListener
            }
            CallHelper().call(context!!, mobileNumber!!.toInt())
        }

        binding.confirm.setOnClickListener {
            progressDialog.show(childFragmentManager,"")
            Log.d(TAG, "confirm: $i")
            nextOrderFragVM.updateOrderStatus(getSheetId(),tabId,getOrderStatusIndex(),i+1,"confirmed")
                    .observe(viewLifecycleOwner, Observer {
                        getNextOrder()
                    })

        }



        return binding.root
    }

    private fun getNextOrder(){
        if (!progressDialog.isAdded)
        progressDialog.show(childFragmentManager,"")
        binding.call.isEnabled=false
        var sheet_id=SharedHelper.getKey(context,"sheet_id")
        val order_status_index=SharedHelper.getIntegerValue(context,"order_status_index")
        val tab_index=SharedHelper.getIntegerValue(context,"tab_index")

        nextOrderFragVM.getSpreadSheetData(sheet_id)
                .observe(viewLifecycleOwner, Observer {res->
                    val rows = res.model?.sheets?.get(tab_index)?.data?.get(0)?.rowData
                    tabId= res.model?.sheets?.get(tab_index)?.properties?.title
                    if (rows != null) {
                        if (rows.isEmpty()){
                            progressDialog.dismiss()
                            Toasty.error(context!!,"no orders ",Toasty.LENGTH_LONG).show()
                            return@Observer
                        }

                        i=0;
                        for (row in rows){
                            val columns=row.values
                            Log.d(TAG, "columns: $columns")
                            if (columns!=null&&columns.size<=order_status_index){
                                populateOrder(row.values)
                                return@Observer
                            }else{
                                Log.d(TAG, "getNextOrder: ==============")
                                val order_status= columns?.get(order_status_index)?.formattedValue
                                if (order_status.equals("waiting_confirmtion") ||order_status==null){
                                    populateOrder(row.values)
                                    return@Observer
                                }
                            }

                            i++

                        }
                        Log.d(TAG, "end for: ")
                        progressDialog.dismiss()
                        Toasty.error(context!!,"no orders ",Toasty.LENGTH_LONG).show()

                    }else{
                        progressDialog.dismiss()
                        Toasty.error(context!!,"no orders ",Toasty.LENGTH_LONG).show()
                    }


                })
    }

    private fun populateOrder(rows: ArrayList<Value>?) {
        progressDialog.dismiss()
        Log.d(TAG, "populateOrder: $rows")
        if (rows==null){
            return
        }

        val nameIndex=SharedHelper.getIntegerValue(context,"name_index")
        val mobileIndex=SharedHelper.getIntegerValue(context,"mobile_index")
        val addressIndex=SharedHelper.getIntegerValue(context,"address_index")
        val order_status_index=SharedHelper.getIntegerValue(context,"order_status_index")


        Log.d(TAG, "populateOrder: $mobileIndex")



        mobileNumber=rows[mobileIndex].formattedValue
        if (mobileNumber==null)binding.call.isEnabled=false
        else binding.call.isEnabled=true


        if (nameIndex>-1) binding.clientName.setText(rows[nameIndex].formattedValue.toString())
        if (mobileIndex>-1) binding.mobileNum.setText(rows[mobileIndex].formattedValue.toString())
        if (addressIndex>-1) binding.clientAddress.setText(rows[addressIndex].formattedValue.toString())
        if (order_status_index>-1&&order_status_index<rows.size) binding.orderStatus.setText(rows[order_status_index].formattedValue.toString())
        progressDialog.dismiss()


    }

    fun getSheetId(): String {
        return SharedHelper.getKey(context,"sheet_id")
    }


    fun getOrderStatusIndex(): Int {
        return SharedHelper.getIntegerValue(context,"order_status_index")

    }
}