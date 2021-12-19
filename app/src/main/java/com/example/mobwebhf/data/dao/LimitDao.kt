package com.example.mobwebhf.data.dao

import androidx.room.*
import com.example.mobwebhf.data.Limit

@Dao
interface LimitDao {
    @Query("SELECT * FROM limittable")
    fun getAll(): List<Limit>

    @Query("SELECT * FROM limittable WHERE id = :idx")
    fun getLimitById(idx: Long?): List<Limit>

    @Insert
    fun insert(limit: Limit): Long

    @Update
    fun update(limit: Limit)

    @Delete
    fun deleteLimit(limit: Limit)
}