package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("commentId")
    val commentId: String,
    @SerializedName("deception")
    val deception: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isEnabled")
    val isEnabled: Int,
    @SerializedName("status")
    val status: String
)