package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Slider(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: List<ModelXXXXXXXX>,
    @SerializedName("msg")
    val msg: String
)