package com.tajr.tajr.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tajr.tajr.models.SpreadSheetRes
import com.tajr.tajr.server.BaseClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SpreadSheetsRepo {

    lateinit var spreadSheetRes:MutableLiveData<com.tajr.tajr.models.Response<SpreadSheetRes>>
    val SPREDADsHEET_URL="https://sheets.googleapis.com/v4/spreadsheets/"
    private  val TAG = "SpreadSheetsRepo"


    fun getSpreadSheetData(spreadsheetId :String?): MutableLiveData<com.tajr.tajr.models.Response<SpreadSheetRes>> {
        spreadSheetRes= MutableLiveData()
        val url=SPREDADsHEET_URL+spreadsheetId
        BaseClient
                .getApiService()
                .getSpreadSheet(url,true)
                .enqueue(object : Callback<SpreadSheetRes> {
                    override fun onResponse(call: Call<SpreadSheetRes>, response: Response<SpreadSheetRes>) {
                        val res=response.body()
                        if (response.isSuccessful&&res!=null){

                            spreadSheetRes.value=com.tajr.tajr.models.Response(res,false,null)

                        }else{
                            spreadSheetRes.value=com.tajr.tajr.models.Response(res,false,"error loading spread sheet")

                        }

                        Log.d(TAG, "onResponse: "+response.body())
                    }

                    override fun onFailure(call: Call<SpreadSheetRes>, t: Throwable) {
                        Log.d(TAG, "onFailure: "+t.message)
                        spreadSheetRes.value=com.tajr.tajr.models.Response(null,false,"error loading spread sheet")

                    }

                })
        return spreadSheetRes
    }
}