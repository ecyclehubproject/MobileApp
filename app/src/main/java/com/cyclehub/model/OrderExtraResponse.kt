package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class OrderExtraResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: OrderExtraResponseData,
    @SerializedName("msg")
    val msg: String
)