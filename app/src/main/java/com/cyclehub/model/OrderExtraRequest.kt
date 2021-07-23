package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class OrderExtraRequest(
    @SerializedName("deception")
    val deception: String? = null,
    @SerializedName("partsInfo")
    val partsInfo: String? = null,
    @SerializedName("brandInfo")
    val brandInfo: String? = null,
    @SerializedName("mediaId")
    val mediaId: String? = null,
    @SerializedName("id")
    val id: String

)