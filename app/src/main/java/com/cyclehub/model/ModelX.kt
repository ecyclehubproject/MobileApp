package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ModelX(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("createdOn")
    val createdOn: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("mode")
    val mode: String,
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("transactionId")
    val transactionId: String,
    @SerializedName("updatedOn")
    val updatedOn: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("vehicleId")
    val vehicleId: Int
)