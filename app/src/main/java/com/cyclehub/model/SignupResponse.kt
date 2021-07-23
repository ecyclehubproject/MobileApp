package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: Any? = null,
    @SerializedName("msg")
    val msg: String
)