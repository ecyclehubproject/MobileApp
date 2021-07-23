package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ServiceX(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)