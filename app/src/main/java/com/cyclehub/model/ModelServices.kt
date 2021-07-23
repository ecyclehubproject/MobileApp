package com.cyclehub.model


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "services")
data class ModelServices(
    @PrimaryKey
    var id: Int,
    var amount: Int,
    var default: Int,
    var description: String,
    var enable: Int,
    var icon: String,
    var image: String,
    var name: String
) {
    constructor() : this(0, 0, 0,"", 0, "", "", "")

}