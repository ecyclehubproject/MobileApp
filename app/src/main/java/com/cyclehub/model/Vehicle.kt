package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Vehicle(
    @SerializedName("brandId")
    val brandId: Int,
    @SerializedName("categoryType")
    val categoryType: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isEnabled")
    val isEnabled: Int,
    @SerializedName("name")
    val name: String
)