package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class TransactionResponce(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: ModelX,
    @SerializedName("msg")
    val msg: String
)