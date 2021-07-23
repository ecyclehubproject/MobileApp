package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class UserId(
    @SerializedName("authToken")
    val authToken: String,
    @SerializedName("emailId")
    val emailId: String,
    @SerializedName("enable")
    val enable: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("verifiedEmail")
    val verifiedEmail: Int
)