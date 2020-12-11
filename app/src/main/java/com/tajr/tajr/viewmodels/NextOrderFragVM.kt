package com.tajr.tajr.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tajr.tajr.models.Response
import com.tajr.tajr.models.SheetValueRes
import com.tajr.tajr.models.SpreadSheetRes
import com.tajr.tajr.repository.SpreadSheetsRepo

class NextOrderFragVM(application: Application) :AndroidViewModel(application) {

    fun getSpreadSheetData(spreadsheetId :String?): MutableLiveData<Response<SpreadSheetRes>> {
        return SpreadSheetsRepo.getSpreadSheetData(spreadsheetId)
    }

    fun updateOrderStatus(sheetId:String,tabId :String?,columnIndex :Int,rowIndex:Int,newValue :String): MutableLiveData<Response<SheetValueRes>> {
        return SpreadSheetsRepo.updateOrderStatus(sheetId,tabId,columnIndex,rowIndex,newValue)
    }

    }