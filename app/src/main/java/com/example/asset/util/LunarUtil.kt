package com.example.asset.util

import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle.FULL
import java.util.Locale

class LunarUtil {
    companion object{
        fun getDayStr(): String {
            // 获取当前日期
            val currentDate = LocalDate.now()
            // 格式化公历日期
            val formatter = DateTimeFormatter.ofPattern("yyyy年M月d日")
            val formattedDate = currentDate.format(formatter)
            // 获取星期几
            val dayOfWeek = currentDate.dayOfWeek.getDisplayName(FULL, Locale.CHINA)
            // 获取农历日期
            val solar = Solar(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth)
            val lunar = Lunar(solar)
            val ganZhiYear = lunar.yearInGanZhi;
            // 获取农历月份
            val lunarMonth = lunar.monthInChinese;
            // 获取农历日期
            val lunarDay = lunar.dayInChinese;
            // 返回完整农历日期字符串，例如"己丑年正月十四"
            val lunarDate = "${ganZhiYear}年${lunarMonth}月$lunarDay"
            return "$formattedDate，$dayOfWeek，$lunarDate"
        }
    }

}