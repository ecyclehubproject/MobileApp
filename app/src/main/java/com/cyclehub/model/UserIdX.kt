package com.cyclehub.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserIdX(
    @SerializedName("authToken")
    val authToken: String,
    @SerializedName("createdAt")
    val createdAt: String,
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
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("verifiedEmail")
    val verifiedEmail: Int
) : Parcelable