package com.cyclehub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderExtraInfo(
     val mediaId: String,
    val brandsInfo: String,
    val partsInfo: String,
    val additionalInfo: String
) : Parcelable
