package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class VehicleDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("isEnabled")
    val isEnabled: Int,
    @SerializedName("name")
    val name: String
)