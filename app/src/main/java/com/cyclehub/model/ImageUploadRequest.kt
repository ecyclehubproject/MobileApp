package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ImageUploadRequest(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("fileName")
    val fileName: String
)