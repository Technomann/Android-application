package com.example.mobwebhf.data.dao

import androidx.room.*
import com.example.mobwebhf.data.Account

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounttable")
    fun getAll(): List<Account>

    @Query("SELECT id FROM accounttable WHERE id = :idx")
    fun getIdById(idx: Long?): List<Int> //ITT EZ AMÚGY MIÉRT NE LEHETNE LONG?

    @Query("SELECT * FROM accounttable WHERE account_number = :a_number")
    fun getAccountByNumber(a_number: String): List<Account>

    @Query("SELECT * FROM accounttable WHERE id = :idx")
    fun getAccountById(idx: Long?): List<Account>

    @Query("UPDATE accounttable SET limitId = NULL WHERE account_number = :a_number")
    fun resetLimitId(a_number: String)

    @Insert
    fun insert(account: Account): Long

    @Update
    fun update(account: Account)

    @Delete
    fun deleteItem(account: Account)
}