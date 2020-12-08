package com.tajr.tajr.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tajr.tajr.models.Response
import com.tajr.tajr.models.SheetValueRes
import com.tajr.tajr.models.SpreadSheetRes
import com.tajr.tajr.repository.SpreadSheetsRepo

class SpreadSheetFragVM(application: Application) :AndroidViewModel(application) {

    fun getSpreadSheetData(spreadsheetId :String?): MutableLiveData<Response<SpreadSheetRes>> {
        return SpreadSheetsRepo.getSpreadSheetData(spreadsheetId)
    }

    fun addOrderStatusColumn(sheetId:String,tabId :String,cellsCount :Int): MutableLiveData<Response<SheetValueRes>> {
        return SpreadSheetsRepo.addOrderStatusColumn(sheetId,tabId,cellsCount)
    }


    }