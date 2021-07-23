package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class VendorSignupRequest(
    @SerializedName("address1")
    val address1: String,
    @SerializedName("address2")
    val address2: String,
    @SerializedName("address3")
    val address3: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("emailId")
    val emailId: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("pincode")
    val pincode: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("state")
    val state: String
)