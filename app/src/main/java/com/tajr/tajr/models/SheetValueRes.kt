package com.tajr.tajr.models

data class SheetValueRes(
        var spreadsheetId:String,
        var updatedRange:String,
        var updatedRows:Int,
        var updatedColumns:Int,
        var updatedCells:Int

)
{
}