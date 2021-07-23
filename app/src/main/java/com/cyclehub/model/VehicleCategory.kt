package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class VehicleCategory(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: List<VehicleBrandDetails>,
    @SerializedName("msg")
    val msg: String
)