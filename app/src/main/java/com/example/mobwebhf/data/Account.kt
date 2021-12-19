package com.example.mobwebhf.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounttable")
data class Account (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "account_number") var account_number: String,
    @ColumnInfo(name = "value") var value: Double,
    @ColumnInfo(name = "limitId") var limitId: Long? = null
    )