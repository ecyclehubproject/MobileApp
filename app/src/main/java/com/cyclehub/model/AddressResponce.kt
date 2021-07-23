package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class AddressResponce(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: AddressInfo,
    @SerializedName("msg")
    val msg: String
)

data class GetAddress(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: List<AddressInfo>,
    @SerializedName("msg")
    val msg: String
)