package com.tajr.tajr.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

class CallHelper {

    fun call(context: Context, number :Int){
        val callIntent=Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number))
        context.startActivity(callIntent)


    }
}