package com.cyclehub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PurchaseData(
    val serviceName: String,
    val price: Double,
    val productId: String,
    val vehicleId: Int = -1,
) : Parcelable