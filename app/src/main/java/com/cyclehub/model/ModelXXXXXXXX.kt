package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ModelXXXXXXXX(
    @SerializedName("id")
    val id: Int,
    @SerializedName("isEnabled")
    val isEnabled: Int,
    @SerializedName("key")
    val key: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("value")
    val value: String
)