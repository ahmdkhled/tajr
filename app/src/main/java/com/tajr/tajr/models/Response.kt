package com.tajr.tajr.models

data class Response<T:Any>
(
        var model :T?,
        var loading:Boolean,
        var error:String?
){
}