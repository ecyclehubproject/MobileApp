package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class CommentsInfo(
    @SerializedName("commentId")
    val commentId: String,
    @SerializedName("customerId")
    val customerId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isEnabled")
    val isEnabled: Int,
    @SerializedName("mediaId")
    val mediaId: String,
    @SerializedName("orderId")
    val orderId: Int
)