package com.cyclehub.model


import com.google.gson.annotations.SerializedName

data class ModelXXX(
    @SerializedName("activity")
    val activity: List<CommentsInfo>,
    @SerializedName("comments")
    val comments: List<Comment>,
    @SerializedName("media")
    val media: List<Media>,
    @SerializedName("order")
    val order: OrdersList,
    @SerializedName("service")
    val service: Service,
    @SerializedName("transactions")
    val transactions: Transactions,
    @SerializedName("users")
    val users: List<UserInfo>,
    @SerializedName("vehicle")
    val vehicle: Vehicle
)