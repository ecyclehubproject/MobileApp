package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class TransactionX(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("mode")
    val mode: String,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("status")
    val status: String
)