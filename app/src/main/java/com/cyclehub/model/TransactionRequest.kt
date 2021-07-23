package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class TransactionRequest(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("paymentMode")
    val paymentMode: String,
    @SerializedName("productId")
    val productId: Int
)