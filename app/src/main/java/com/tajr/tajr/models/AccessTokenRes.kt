package com.tajr.tajr.models

data class AccessTokenRes
(
        var access_token:String,
        var token_type:String,
        var expires_in:Int
) {
}