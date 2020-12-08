package com.tajr.tajr.models

data class SheetValuePayload (
        var range:String,
        var majorDimension:String,
        var values:ArrayList<ArrayList<String>>
){
}