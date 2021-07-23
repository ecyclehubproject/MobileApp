package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: UserInfo,
    @SerializedName("msg")
    val msg: String
)