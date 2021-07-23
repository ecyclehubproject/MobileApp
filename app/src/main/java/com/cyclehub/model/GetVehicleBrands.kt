package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class GetVehicleBrands(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: List<VehicleDetails>,
    @SerializedName("msg")
    val msg: String
)