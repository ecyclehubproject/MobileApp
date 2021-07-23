package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("mode")
    val mode: String,
    @SerializedName("razorpay_order_id")
    val razorpayOrderId: String = "",
    @SerializedName("razorpay_payment_id")
    val razorpayPaymentId: String = "",
    @SerializedName("razorpay_signature")
    val razorpaySignature: String = "",
    @SerializedName("status")
    val status: String,
    val mediaId: String,
    val brandInfo: String,
    val partsInfo: String,
    val deception: String
)