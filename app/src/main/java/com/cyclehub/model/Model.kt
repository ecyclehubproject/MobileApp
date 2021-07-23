package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Model(
    @SerializedName("auth_token")
    val authToken: String,
    @SerializedName("role")
    val role: String
)