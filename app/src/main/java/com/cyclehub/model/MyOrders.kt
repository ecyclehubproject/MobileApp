package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class MyOrders(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: ModelXXXX,
    @SerializedName("msg")
    val msg: String
)