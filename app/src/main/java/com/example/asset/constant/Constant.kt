package com.example.asset.constant

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.asset.viewmodel.MainViewModel

class Constant {
    companion object{
        // 定义了一个 CompositionLocal ，用于在Compose中跨组件共享 MainViewModel 实例
        val LocalSharedViewModel = staticCompositionLocalOf<MainViewModel> {
            error("No MainViewModel provided")
        }

        // 定义一个CompositionLocal来共享SnackbarHostState
        val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
            error("No SnackbarHostState provided")
        }

        val TYPE_LIST = listOf("定期", "新享定期", "结构性", "理财")
        val BANK_LIST = listOf("哈尔滨银行", "建设银行", "工商银行", "光大银行", "中国银行")
        val NAME_LIST = listOf("张利", "朱广龙", "朱巍", "张翠", "崔法全")
    }
}