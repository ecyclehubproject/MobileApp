package com.cyclehub.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class UserData(
    @PrimaryKey
    val id: Int,
    val emailId: String,
    val enable: Int,
    val name: String,
    val phoneNumber: String,
    val picture: String,
    val role: String,
    val verifiedEmail: Int
) : Parcelable {
    constructor() : this(0, "", 0, "", "", "", "", 0)
}