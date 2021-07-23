package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: Model,
    @SerializedName("msg")
    val msg: String
)