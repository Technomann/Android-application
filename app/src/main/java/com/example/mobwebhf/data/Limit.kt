package com.example.mobwebhf.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "limittable")
data class Limit (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "accountNumber") var accountNumber: String
    )