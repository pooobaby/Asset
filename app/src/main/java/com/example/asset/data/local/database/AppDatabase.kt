package com.example.asset.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.asset.data.local.dao.SavingsDao
import com.example.asset.data.local.entity.Savings

@Database(entities = [Savings::class], version = 4, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun savingsDao(): SavingsDao
}