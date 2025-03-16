
package com.example.asset.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.asset.data.local.entity.Savings
import com.example.asset.data.repository.SavingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _repository = SavingRepository(application)

    private val _showType = MutableLiveData("date")

    var isDark by mutableStateOf(false)     // 主题设置的变量

    // 修改 _showType 的值
    fun changeShowType(newType: String) {
        _showType.value = newType
    }

    val totalSavings: LiveData<Double> = _repository.getTotalSavings()
    val totalIncome: LiveData<Double> = _repository.getTotalIncome()
    // 获取dueDate距离当前日期最近的存款记录
    val nearestSavings: LiveData<Savings> = _repository.getNearestSavings()

    // 根据 ID 查询储蓄记录
    fun getById(id: Int): Flow<Savings> {
        return _repository.getById(id)
    }

    // 更新记录
    fun update(savings: Savings) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _repository.update(savings)
        }
    }

    // _showType.switchMap 会监听 _showType 的变化，
    // 每当 _showType 的值发生变化时， switchMap 中的 lambda 表达式会被执行。
    val groupedSavings: LiveData<Map<String, List<Savings>>> = _showType.switchMap { showType ->
        when (showType) {
            "date" -> _repository.dataByYear
            "type" -> _repository.dateByType
            "bankName" -> _repository.dateByBankName
            else -> _repository.dataByYear
        }
    }

    // withContext(Dispatchers.IO) 来确保数据库操作在后台线程中执行
    fun insert(savings: Savings) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _repository.insert(savings)
        }
    }

    // 删除储蓄记录
    fun delete(savings: Savings) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _repository.delete(savings)
        }
    }


}



