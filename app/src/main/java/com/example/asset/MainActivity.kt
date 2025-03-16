package com.example.asset

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.asset.constant.Constant
import com.example.asset.navigation.MainNavigation
import com.example.asset.navigation.ProvideNavController
import com.example.asset.ui.theme.AssetTheme
import com.example.asset.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.White.value.toInt(),
                darkScrim = Color.Black.value.toInt()
            ),
        )

        setContent {
            // 在Compose的UI树中提供一个共享的 MainViewModel 实例，让所有子组件都可以访问它
            CompositionLocalProvider(
                Constant.LocalSharedViewModel provides viewModel,
            ) {
                AssetTheme {
                    SystemBarsHandler()     // 在 Compose 中处理系统栏样式
                    // 使用 rememberNavController 创建并记住一个 NavController 实例
                    // NavController 是Compose导航的核心，负责管理导航堆栈和屏幕切换
                    // remember 确保在重组时保持相同的实例
                    // ProvideNavController 是一个自定义的CompositionLocalProvider，
                    // 用于在Compose树中共享 NavController 实例
                    // MainNavigation(navController) 是实际的导航组件，定义了应用的导航图
                    // 通过这种方式，所有子组件都可以访问同一个 NavController 实例
                    val navController = rememberNavController()
                    ProvideNavController(navController) { MainNavigation(navController) }
                }
            }
        }
    }
}

// 处理系统栏图标颜色
@Composable
fun SystemBarsHandler() {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window
    val viewModel = Constant.LocalSharedViewModel.current
    val isDark = viewModel.isDark
    // val isDark = isSystemInDarkTheme()

    SideEffect {
        window?.let {
            WindowInsetsControllerCompat(it, view).apply {
                isAppearanceLightNavigationBars = !isDark
                isAppearanceLightStatusBars = !isDark
            }
        }
    }
}
