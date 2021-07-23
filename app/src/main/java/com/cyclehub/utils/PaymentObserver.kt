package com.cyclehub.utils

import androidx.lifecycle.LiveData
import com.cyclehub.model.PaymentRequest
import com.cyclehub.model.RazorPayPayment
import com.cyclehub.other.SingleLiveEvent

object PaymentObserver : LiveData<PaymentRequest>() {
    var paymentStatus: RazorPayPayment?
        get() = paymentStatusLiveData.value
        set(value) {
            paymentStatusLiveData.postValue(value)
        }

    val paymentStatusLiveData = SingleLiveEvent<RazorPayPayment?>()
}