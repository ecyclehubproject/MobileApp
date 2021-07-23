package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("payment_id")
    val paymentId: String
)