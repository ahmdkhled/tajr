package com.tajr.tajr.view.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tajr.tajr.R
import com.tajr.tajr.adapters.SheetTabsAdapter
import com.tajr.tajr.databinding.DialogSheetSettingBinding
import com.tajr.tajr.fragments.SpreadSheetFrag
import com.tajr.tajr.helper.SharedHelper
import com.tajr.tajr.models.Sheet
import es.dmoral.toasty.Toasty

class SheetSettingDialog (var sheets :ArrayList<Sheet>,val sheetId:String):DialogFragment() ,SheetTabsAdapter.OnTabClickListener{

    lateinit var binding: DialogSheetSettingBinding
     var columnes=ArrayList<String>()
    var tabIndex=0
    private val TAG = "SheetSettingDialog"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.dialog_sheet_setting,container,false)


        populateSpinner(0)

        val tabsAdapter=SheetTabsAdapter(sheets,this)
        val layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.tabsRecycler.adapter=tabsAdapter
        binding.tabsRecycler.layoutManager=layoutManager

        binding.cancel.setOnClickListener {
            dismiss()
        }
        binding.save.setOnClickListener {
            val mobileIndex=binding.mobileSpinner.selectedItemPosition
            val nameIndex=binding.nameSpinner.selectedItemPosition
            val addressIndex=binding.addressSpinner.selectedItemPosition
            val orderStatusIndex=binding.orderStatusSpinner.selectedItemPosition
            if (mobileIndex==0||nameIndex==0){
                Toast.makeText(context,"complete all fields",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.save.startAnimation()

            Log.d(TAG, "onCreateView: $tabIndex $mobileIndex  $nameIndex")
            saveData(tabIndex,mobileIndex-1,nameIndex-1,addressIndex,orderStatusIndex)
            if (orderStatusIndex==0){
                val tabId=sheets.get(tabIndex).properties.title
                (parentFragment as SpreadSheetFrag).spreadSheetFragVM.addOrderStatusColumn(sheetId,tabId,
                        sheets[tabIndex].data[0].rowData.size)
                        .observe(viewLifecycleOwner, Observer {res->
                            binding.save.revertAnimation()

                            if (res.error!=null){
                                Toasty.error(context!!,"error adding column",Toasty.LENGTH_LONG).show()
                            }else
                                dismiss()
                        })
            }
        }



        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (dialog != null && dialog!!.window != null)
            dialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onTabClicked(tabIndex: Int) {
        populateSpinner(tabIndex)
        this.tabIndex=tabIndex
    }

    fun populateSpinner(tabIndex:Int){
        columnes.clear()
        val values=sheets.get(tabIndex).data.get(0).rowData.get(0).values

        if (values != null) {
            columnes.add("select column")
            for (value in values){
                if (value.formattedValue!=null)
                columnes.add(value.formattedValue.toString())
            }
        }

        val mobileSpinnerAdapter=ArrayAdapter<String>(context!!,android.R.layout.simple_spinner_dropdown_item,columnes)
        binding.mobileSpinner.adapter=mobileSpinnerAdapter
        binding.nameSpinner.adapter=mobileSpinnerAdapter
        binding.addressSpinner.adapter=mobileSpinnerAdapter
        binding.orderStatusSpinner.adapter=mobileSpinnerAdapter
    }

    fun saveData(tabIndex: Int, mobileIndex: Int, nameIndex: Int, addressIndex: Int, orderStatusIndex: Int){
        SharedHelper.putKey(context,"sheet_id",sheetId)
        SharedHelper.putKey(context,"tab_index",tabIndex)
        SharedHelper.putKey(context,"mobile_index",mobileIndex)
        SharedHelper.putKey(context,"name_index",nameIndex)
        if (addressIndex>0)
            SharedHelper.putKey(context,"address_index",nameIndex)
        if (orderStatusIndex>0)
            SharedHelper.putKey(context,"order_status_index",orderStatusIndex)



    }
}