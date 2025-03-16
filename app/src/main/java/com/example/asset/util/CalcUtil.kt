package com.example.asset.util

import android.util.Log
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

class CalcUtil {

    companion object {
        fun calcIncome(
            type: Int,
            amount: Double,
            rate: Double,
            startDate: Long,
            dueDate: Long
        ): Double {
            val income: Double
            when (type) {
                0, 1 -> {
                    val years = getYearFromTimestamp(dueDate) - getYearFromTimestamp(startDate)
                    income = amount * rate / 100 * years;
                }
                2, 3 -> {
                    val days = getDaysBetweenTimestamps(startDate, dueDate)
                    income = (amount * rate / 100) * (days/ 365.0);
                }
                else -> {
                    income = 0.0
                }
            }

            return income
        }

        private fun getDaysBetweenTimestamps(timestamp1: Long, timestamp2: Long): Long {
            val instant1 = Instant.ofEpochMilli(timestamp1)
            val instant2 = Instant.ofEpochMilli(timestamp2)
            return ChronoUnit.DAYS.between(instant1, instant2).absoluteValue // 计算天数并取绝对值
        }

        private fun getYearFromTimestamp(timestamp: Long): Int {
            val instant = Instant.ofEpochMilli(timestamp) // 将时间戳转换为 Instant
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) // 转换为 LocalDateTime
            return dateTime.year // 获取年份
        }
    }
}