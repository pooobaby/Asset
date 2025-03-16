package com.example.asset.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Savings(
    @PrimaryKey(autoGenerate = true) val uid: Int,          // id主键
    @ColumnInfo(name = "bankName") val bankName: Int?,   // 银行名称
    @ColumnInfo(name = "name") val name: Int?,           // 持有人
    @ColumnInfo(name = "amount") val amount: Double?,        // 金额
    @ColumnInfo(name = "type") val type: Int?,           // 类型
    @ColumnInfo(name = "startDate") val startDate: Long?,   // 起存日
    @ColumnInfo(name = "dueDate") val dueDate: Long?,       // 到期日
    @ColumnInfo(name = "rate") val rate: Double?,           // 利率
    @ColumnInfo(name = "income") val income: Double?,       // 收益
    @ColumnInfo(name = "memo") val memo: String?,           // 备注
)