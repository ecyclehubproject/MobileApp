package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: List<Transactions>,
    @SerializedName("msg")
    val msg: String
)