package com.example.asset.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.asset.constant.Constant
import com.example.asset.navigation.LocalNavController
import com.example.asset.ui.components.ChoiceBank
import com.example.asset.ui.components.ChoiceDueDate
import com.example.asset.ui.components.ChoiceName
import com.example.asset.ui.components.ChoiceStartDate
import com.example.asset.ui.components.ChoiceType
import com.example.asset.ui.components.InputMemo
import com.example.asset.ui.components.InputRate
import com.example.asset.ui.components.InputSaving
import com.example.asset.ui.components.TitleButton
import com.example.asset.ui.components.showIncome
import com.example.asset.ui.theme.AssetTheme

@Composable
fun AppendSavings() {
    val navController = LocalNavController.current

    // 添加 SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    // 定义状态变量
    var bankName by remember { mutableIntStateOf(0) }
    var name by remember { mutableIntStateOf(0) }
    var amount by remember { mutableStateOf("0.00") }
    var type by remember { mutableIntStateOf(0) }
    var startDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    var dueDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    var rate by remember { mutableStateOf("0.00") }
    var income by remember { mutableDoubleStateOf(0.0) }
    var memo by remember { mutableStateOf("") }

    CompositionLocalProvider(Constant.LocalSnackbarHostState provides snackbarHostState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.systemBarsPadding()
        ) {
            TitleButton(
                navController, "append", bankName, name,
                amount, type, rate, startDate,
                dueDate, income, memo, 0
            )
            SnackbarHost(hostState = snackbarHostState)
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            ) {
                ChoiceType(onTypeSelected = { selectedIndex ->
                    type = selectedIndex
                }, 0)
                ChoiceBank(onTypeSelected = { selectedIndex ->
                    bankName = selectedIndex
                }, 0)
                ChoiceName(onTypeSelected = { selectedIndex ->
                    name = selectedIndex
                }, 0)
                InputSaving(initValue = amount, onValueChange = { newText ->
                    amount = newText
                })
                InputRate(initValue = rate, onValueChange = { newText ->
                    rate = newText
                })
                ChoiceStartDate(initStartDate = startDate,
                    onStartDateChanged = { newDate ->
                        startDate = newDate
                    })

                ChoiceDueDate(initDueDate = dueDate,
                    onStartDateChanged = { newDate ->
                        dueDate = newDate
                    })
                income = showIncome(type, amount, rate, startDate, dueDate, 0.0)

                InputMemo(initValue = memo, onValueChange = { newText ->
                    memo = newText
                })
            }

        }
        }
    }
}

// 提供一个模拟的 NavController 实例
@Preview
@Composable
fun PreviewAddSavings() {
    val mockNavController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides mockNavController) {
        AssetTheme {
            // AppendSavings()
        }
    }
}
