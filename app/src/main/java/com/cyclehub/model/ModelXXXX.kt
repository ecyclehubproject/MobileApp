package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ModelXXXX(
    @SerializedName("orders")
    val orders: List<OrderX>,
    @SerializedName("services")
    val services: List<ServiceX>,
    @SerializedName("transactions")
    val transactions: List<TransactionX>,
    @SerializedName("users")
    val users: List<UserX>
)