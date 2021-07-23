package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ImageUploadData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("isEnabled")
    val isEnabled: Int,
    @SerializedName("mediaId")
    val mediaId: String,
    @SerializedName("mediaPath")
    val mediaPath: String,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("status")
    val status: String
)