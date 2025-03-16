package com.example.asset.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.asset.constant.Constant

private val DarkColorScheme = darkColorScheme(
    primary = Blue200,
    primaryContainer = Black400,
    secondary = Black200,
    background = Black300,
    surface = Blue200,
    onPrimary = White100,
    onSecondary = White100,
    onSurface = Green200,
    onBackground = White100,
    onError = Red300
)

private val LightColorScheme = lightColorScheme(
    primary = Blue200,
    primaryContainer = Yellow100,
    secondary = Black100,
    background = White100,
    surface = Blue200,
    onPrimary = White100,
    onSecondary = Black300,
    onSurface = Red300,
    onBackground = Black300,
    onError = Red300
)

@Composable
fun AssetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 禁用动态颜色
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val viewModel = Constant.LocalSharedViewModel.current
    val isDark = viewModel.isDark
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}