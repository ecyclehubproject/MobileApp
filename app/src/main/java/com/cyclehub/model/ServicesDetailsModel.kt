package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ServicesDetailsModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: ModelServices,
    @SerializedName("msg")
    val msg: String
)