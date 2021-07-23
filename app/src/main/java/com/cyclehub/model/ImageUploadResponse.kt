package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("model")
    val model: ImageUploadData,
    @SerializedName("msg")
    val msg: String
)