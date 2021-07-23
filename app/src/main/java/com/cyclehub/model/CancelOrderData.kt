package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class CancelOrderData(
    @SerializedName("createdOn")
    val createdOn: String,
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
    @SerializedName("updatedOn")
    val updatedOn: String,
    @SerializedName("vehicleId")
    val vehicleId: Int,
    @SerializedName("vendorId")
    val vendorId: Int
)