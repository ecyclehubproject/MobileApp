package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class OrderById(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: ModelXXX,
    @SerializedName("msg")
    val msg: String
)