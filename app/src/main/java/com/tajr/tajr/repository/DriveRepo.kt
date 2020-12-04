package com.tajr.tajr.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tajr.tajr.helper.App
import com.tajr.tajr.helper.SharedHelper
import com.tajr.tajr.models.DriveRes
import com.tajr.tajr.models.File
import com.tajr.tajr.server.BaseClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriveRepo  {

    private  val TAG = "DriveRepo"
    val driveUrl="https://www.googleapis.com/drive/v3/files?q=mimeType='application/vnd.google-apps.spreadsheet'"
    lateinit var  spreadSheets:MutableLiveData<ArrayList<File>>

    fun getAllSpreadSheets(): MutableLiveData<ArrayList<File>> {
        spreadSheets= MutableLiveData()
        BaseClient.
                getApiService()
                .getSpreadSheets(driveUrl)
                .enqueue(object :Callback<DriveRes>{
                    override fun onResponse(call: Call<DriveRes>, response: Response<DriveRes>) {
                        Log.d(TAG, "onResponse: ")
                        val driveRes=response.body();
                        if (response.isSuccessful&&driveRes!=null){
                            spreadSheets.value=driveRes.files
                        }else{
                            val error =response.errorBody()?.string()
                            Log.d(TAG, "onResponse: "+error)

                        }
                    }
                    override fun onFailure(call: Call<DriveRes>, t: Throwable) {
                        Log.d(TAG, "onFailure: ")
                    }



                })
        return spreadSheets
    }
}