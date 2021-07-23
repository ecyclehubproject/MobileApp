package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class GetEnineerVendorResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: List<EngineerData>,
    @SerializedName("msg")
    val msg: String
)