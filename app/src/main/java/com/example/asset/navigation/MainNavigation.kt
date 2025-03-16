package com.example.asset.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.asset.ui.screen.AppendSavings
import com.example.asset.ui.screen.HomeScreen
import com.example.asset.ui.screen.UpdateSavings
import com.example.asset.ui.theme.AssetTheme

// compositionLocalOf这是Compose提供的一个工具，用于创建可以在Compose树中共享的全局变量
// 与 staticCompositionLocalOf 不同， compositionLocalOf 在值改变时会触发重组
val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

// 这是一个自定义的Composable函数，用于在Compose树中提供 NavController 实例
@Composable
fun ProvideNavController(navController: NavController, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalNavController provides navController) {
        content()
    }
}

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen()
        }
        composable("append") {
            AssetTheme {
                AppendSavings()
            }
        }
        composable(
            route = "update/{savingsId}",
            arguments = listOf(
                navArgument("savingsId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // 从导航参数中获取 savingsId
            val savingsId = backStackEntry.arguments?.getInt("savingsId") ?: 0
            UpdateSavings(savingsId)
        }
    }
}
