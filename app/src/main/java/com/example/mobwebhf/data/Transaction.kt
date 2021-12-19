package com.example.mobwebhf.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transactiontable")
data class Transaction (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "amount") var amount: Double,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "income") var income: Boolean,
    @ColumnInfo(name = "accountId") var accountId: Long? = null
    )

