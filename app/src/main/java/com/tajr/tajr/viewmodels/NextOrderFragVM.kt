package com.tajr.tajr.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tajr.tajr.models.Response
import com.tajr.tajr.models.SpreadSheetRes
import com.tajr.tajr.repository.SpreadSheetsRepo

class NextOrderFragVM(application: Application) :AndroidViewModel(application) {

    fun getSpreadSheetData(spreadsheetId :String?): MutableLiveData<Response<SpreadSheetRes>> {
        return SpreadSheetsRepo.getSpreadSheetData(spreadsheetId)
    }
    }