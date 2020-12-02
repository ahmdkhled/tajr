package com.tajr.tajr.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tajr.tajr.models.File
import com.tajr.tajr.repository.DriveRepo
import com.tajr.tajr.repository.GsignInRepo

class AllSheetsFragVM(application: Application) : AndroidViewModel(application) {

    fun getAllSpreadSheets(): MutableLiveData<ArrayList<File>>{
        return DriveRepo().getAllSpreadSheets()
    }

    fun getAccessToken(refresh_token :String,client_id:String,client_secret:String) {
        return GsignInRepo.getAccessToken(refresh_token,client_id,client_secret)
    }

    }