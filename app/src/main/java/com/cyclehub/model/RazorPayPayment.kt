package com.cyclehub.model

import com.razorpay.PaymentData

data class RazorPayPayment(val rzpPaymentId: String = "", val paymentData: PaymentData)