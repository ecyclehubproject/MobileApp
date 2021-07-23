package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class RazorPayError(
    @SerializedName("error")
    val error: Error
)