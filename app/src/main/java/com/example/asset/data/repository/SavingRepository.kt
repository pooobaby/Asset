package com.example.asset.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.Room
import com.example.asset.constant.Constant
import com.example.asset.data.local.database.AppDatabase
import com.example.asset.data.local.entity.Savings
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class SavingRepository(application: Application) {
    // 初始化Room数据库
    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "Asset.db"
    ).build()

    // 获取Savings表的DAO对象
    private val savingsDao = database.savingsDao()

    // 直接返回 LiveData<Map<Int, List<Savings>>>
    val dataByYear: LiveData<Map<String, List<Savings>>> = savingsDao.getAll().map { savingsList ->
        savingsList.groupBy { savings ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = savings.dueDate ?: calendar.timeInMillis
            calendar.get(Calendar.YEAR).toString() + "年"
        }.mapValues { (_, list) ->
            list.sortedBy { it.dueDate ?: 0L }
        }
    }

    val dateByType: LiveData<Map<String, List<Savings>>> = savingsDao.getAll().map { savingsList ->
        savingsList.groupBy { savings ->
            Constant.TYPE_LIST[savings.type!!]
        }.mapValues { (_, list) ->
            list.sortedBy { it.dueDate ?: 0L }
        }
    }

    val dateByBankName: LiveData<Map<String, List<Savings>>> =
        savingsDao.getAll().map { savingsList ->
            savingsList.groupBy { savings ->
                Constant.BANK_LIST[savings.bankName!!]
            }.mapValues { (_, list) ->
                list.sortedBy { it.dueDate ?: 0L }
            }
        }

    // 插入一条记录
    fun insert(savings: Savings) = savingsDao.insert(savings)

    // 更新记录
    fun update(savings: Savings) = savingsDao.update(savings)

    // 删除一条记录
    fun delete(savings: Savings) = savingsDao.delete(savings)

    // 计算并返回所有记录的总金额
    fun getTotalSavings(): LiveData<Double> {
        return savingsDao.getAll().map { savingsList ->
            savingsList.sumOf { (it.amount?:0.0).toDouble() }
        }
    }

    // 计算并返回所有记录的总收入
    fun getTotalIncome(): LiveData<Double> {
        return savingsDao.getAll().map { savingsList ->
            savingsList.sumOf { it.income ?: 0.0 }
        }
    }

    // 根据 ID 查询储蓄记录
    fun getById(id: Int): Flow<Savings> {
        return savingsDao.getById(id)
    }

    // 获取dueDate距离当前日期最近的存款记录
    fun getNearestSavings(): LiveData<Savings> = savingsDao.getNearestSavings()



}