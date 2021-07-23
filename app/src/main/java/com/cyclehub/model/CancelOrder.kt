package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class CancelOrder(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: CancelOrderData,
    @SerializedName("msg")
    val msg: String
)