package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Orders(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: List<OrdersList>,
    @SerializedName("msg")
    val msg: String
)