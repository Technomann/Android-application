package com.example.mobwebhf.data.dao

import androidx.room.*
import com.example.mobwebhf.data.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactiontable")
    fun getAll(): List<Transaction>

    @Query("SELECT * FROM transactiontable WHERE accountId = :idx")
    fun getAllByAccountId(idx: Long?): List<Transaction>

    @Query("SELECT * FROM transactiontable WHERE accountId = (SELECT id FROM accounttable WHERE account_number = :number)")
    fun getAllByAccountNumber(number: String): List<Transaction>

    @Insert
    fun insert(transaction: Transaction): Long

    @Update
    fun update(transaction: Transaction)

    @Delete
    fun deleteItem(transaction: Transaction)
}