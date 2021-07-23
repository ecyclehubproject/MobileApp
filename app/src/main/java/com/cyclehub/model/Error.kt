package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("field")
    val `field`: Any,
    @SerializedName("metadata")
    val metadata: Metadata,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("step")
    val step: String
)