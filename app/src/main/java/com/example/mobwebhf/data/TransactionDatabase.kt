package com.example.mobwebhf.data

import com.example.mobwebhf.data.dao.TransactionDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobwebhf.data.dao.AccountDao
import com.example.mobwebhf.data.dao.LimitDao

@Database(entities = [Account::class, Transaction::class, Limit::class], version = 1)
abstract class TransactionDatabase : RoomDatabase(){

    abstract fun accountDao(): AccountDao
    abstract fun limitDao(): LimitDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        fun getDatabase(applicationContext: Context): TransactionDatabase {
            return Room.databaseBuilder(
                applicationContext,
                TransactionDatabase::class.java,
                "transactions"
            ).build();
        }
    }
}