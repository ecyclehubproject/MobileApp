package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class OrderAssignRequest(
    @SerializedName("engineerId")
    val engineerId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("status")
    val status: String
)