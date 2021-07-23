package com.cyclehub.model


import com.google.gson.annotations.SerializedName


data class ServicesModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: List<ModelServices>,
    @SerializedName("msg")
    val msg: String
)