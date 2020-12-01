package com.tajr.tajr.models

data class RefreshTokenRes (
        var access_token:String,
        var refresh_token:String,
        var id_token:String,
        var expires_in:Int
){
}