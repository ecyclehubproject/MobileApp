package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("emailId")
    val emailId: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("role")
    val role: String
)

data class AddEngineerRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("emailId")
    val emailId: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)