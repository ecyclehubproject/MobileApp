package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class PaymentResponce(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: ModelXX,
    @SerializedName("msg")
    val msg: String
)
