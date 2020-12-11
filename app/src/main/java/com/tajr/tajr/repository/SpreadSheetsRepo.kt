package com.tajr.tajr.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tajr.tajr.models.SheetValuePayload
import com.tajr.tajr.models.SheetValueRes
import com.tajr.tajr.models.SpreadSheetRes
import com.tajr.tajr.server.BaseClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SpreadSheetsRepo {

    lateinit var spreadSheetRes:MutableLiveData<com.tajr.tajr.models.Response<SpreadSheetRes>>
    lateinit var sheetValueRes:MutableLiveData<com.tajr.tajr.models.Response<SheetValueRes>>

    val SPREDADsHEET_URL="https://sheets.googleapis.com/v4/spreadsheets/"
    private  val TAG = "SpreadSheetsRepo"
    private val A1_NOTATION="ABCDEFGHIJKLMNOPQRSTUVWXYZAABBCCDDEEFFGGHHIIJJKKLLMMNNOOPPQQRRSSTTUUVVWWXXYYZZ"
    private val valueInputOption="USER_ENTERED"


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

    fun addOrderStatusColumn(sheetId:String,tabId :String,cellsCount :Int): MutableLiveData<com.tajr.tajr.models.Response<SheetValueRes>> {
        Log.d(TAG, "addOrderStatusColumn: "+cellsCount)
        sheetValueRes= MutableLiveData()
        val range=tabId+"!"+A1_NOTATION.toCharArray()[cellsCount]+"1"
        val url= SPREDADsHEET_URL+sheetId+"/values/"+ range
        val column=ArrayList<ArrayList<String>>()
        val list=ArrayList<String>()
        list.add("order_status")
        column.add(list)
        val paylaod=SheetValuePayload(range,"ROWS",column)
        addColumn(url,paylaod, valueInputOption)


        return sheetValueRes

    }

    fun updateOrderStatus(sheetId:String, tabId: String?, columnIndex:Int, rowIndex:Int, newValue:String): MutableLiveData<com.tajr.tajr.models.Response<SheetValueRes>> {
        sheetValueRes= MutableLiveData()

        val range=tabId+"!"+A1_NOTATION.toCharArray()[columnIndex]+rowIndex
        Log.d(TAG, "updateOrderStatus: $range")
        val url= SPREDADsHEET_URL+sheetId+"/values/"+ range
        val column=ArrayList<ArrayList<String>>()
        val list=ArrayList<String>()
        list.add(newValue)
        column.add(list)
        val paylaod=SheetValuePayload(range,"ROWS",column)

        addColumn(url,paylaod, valueInputOption)

        return sheetValueRes

    }


    fun addColumn(url: String, paylaod: SheetValuePayload, valueInputOption: String) {
        BaseClient
                .getApiService()
                .addColumn(url,paylaod, SpreadSheetsRepo.valueInputOption)
                .enqueue(object:Callback<SheetValueRes>{
                    override fun onResponse(call: Call<SheetValueRes>, response: Response<SheetValueRes>) {
                        val res=response.body()
                        if (response.isSuccessful&&res!=null){
                            sheetValueRes.value=com.tajr.tajr.models.Response(res,false,null)
                        }else
                            sheetValueRes.value=com.tajr.tajr.models.Response(null,false,"error adding column")

                    }

                    override fun onFailure(call: Call<SheetValueRes>, t: Throwable) {
                        sheetValueRes.value=com.tajr.tajr.models.Response(null,false,"error adding column")

                    }

                })
    }

}