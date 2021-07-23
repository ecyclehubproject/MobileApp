package com.cyclehub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationData(
    val address: String,
    val city: String,
    val state: String,
    val pincode: String,
    val country: String,
    val latitude: String,
    val longitude: String
) : Parcelable
