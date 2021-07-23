package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class VendorOrderConfirmResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: ModelXXXXXXX,
    @SerializedName("msg")
    val msg: String
)