package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ModelXXXXXX(
    @SerializedName("enable")
    val enable: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("UserId")
    val userId: UserId,
    @SerializedName("vendorId")
    val vendorId: Int
)