package com.cyclehub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetails(val id: Int, val orderId: String, val price: Double) : Parcelable
