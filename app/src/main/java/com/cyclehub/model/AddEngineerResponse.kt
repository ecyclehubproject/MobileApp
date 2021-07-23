package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class AddEngineerResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: ModelXXXXXX,
    @SerializedName("msg")
    val msg: String
)