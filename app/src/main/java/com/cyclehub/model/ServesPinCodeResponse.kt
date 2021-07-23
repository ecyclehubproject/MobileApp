package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ServesPinCodeResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: ModelXXXXX,
    @SerializedName("msg")
    val msg: String
)