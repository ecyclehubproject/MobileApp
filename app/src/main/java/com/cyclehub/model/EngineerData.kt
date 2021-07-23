package com.cyclehub.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EngineerData(
    @SerializedName("enable")
    val enable: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("UserId")
    val userId: UserIdX,
    @SerializedName("vendorId")
    val vendorId: Int
) : Parcelable {

    override fun toString(): String {
        return userId.name
    }
}