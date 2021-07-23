package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("default")
    val default: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("enable")
    val enable: Int,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
)