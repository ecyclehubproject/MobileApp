package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("emailId")
    val emailId: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("name")
    val name: String
)