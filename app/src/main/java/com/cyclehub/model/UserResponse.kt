package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: UserData,
    @SerializedName("msg")
    val msg: String
)