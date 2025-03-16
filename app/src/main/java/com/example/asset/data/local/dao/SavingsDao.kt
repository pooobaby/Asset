package com.example.asset.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asset.data.local.entity.Savings
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsDao {
    // 查询所有储蓄记录
    @Query("SELECT * FROM savings")
    fun getAll(): LiveData<List<Savings>>

    // 根据 ID 查询储蓄记录
    @Query("SELECT * FROM savings WHERE uid = :id")
    fun getById(id: Int): Flow<Savings>

    // 插入单条储蓄记录
    @Insert
    fun insert(savings: Savings): Long

    // 插入多条记录
    @Insert
    fun insertAll(vararg savings: Savings): List<Long>

    // 更新记录
    @Update
    fun update(savings: Savings): Int

    // 删除记录
    @Delete
    fun delete(savings: Savings): Int

    // 根据 ID 删除记录
    @Query("DELETE FROM savings WHERE uid = :id")
    fun deleteById(id: Int): Int

    // 获取dueDate距离当前日期最近的存款记录
    @Query("SELECT * FROM savings ORDER BY ABS(dueDate - strftime('%s','now') * 1000) LIMIT 1")
    fun getNearestSavings(): LiveData<Savings>
}