package com.cyclehub.utils

import androidx.lifecycle.LiveData
import com.cyclehub.other.SingleLiveEvent

object PaymentResult : LiveData<Boolean>() {
    private val paymentResult = SingleLiveEvent<Boolean?>()
    val results: LiveData<Boolean?>
        get() = paymentResult
}