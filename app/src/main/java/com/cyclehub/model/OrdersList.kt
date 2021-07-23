package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class OrdersList(
    @SerializedName("customerId")
    val customerId: Int,
    @SerializedName("engineerId")
    val engineerId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isEnabled")
    val isEnabled: Int,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("serviceId")
    val serviceId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("vehicleId")
    val vehicleId: Int,
    @SerializedName("vendorId")
    val vendorId: Int,
    @SerializedName("transactionId")
    val transactionId: String,
    @SerializedName("createdOn")
    val createdOn: String,
    @SerializedName("partsInfo")
    val partsInfo: String,
    @SerializedName("brandInfo")
    val brandInfo: String,
    @SerializedName("mode")
    val mode: String,
)