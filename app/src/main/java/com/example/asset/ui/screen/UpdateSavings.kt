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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.asset.constant.Constant
import com.example.asset.data.local.entity.Savings
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
import java.text.DecimalFormat

@Composable
fun UpdateSavings(id: Int) {
    val navController = LocalNavController.current
    val viewModel = Constant.LocalSharedViewModel.current
    val testSavings = Savings(
        uid = 0, bankName = 1, name = 0, amount = 250000.00,
        type = 0, startDate = 0, dueDate = 0, rate = 2.5, income = 5000.00, memo = "test"
    )
    val decimalFormat = DecimalFormat("#,##0.00")

    var savings by remember { mutableStateOf(testSavings) }
    var isQueryFinished by remember { mutableStateOf(false) }
    // 添加 SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(id) {
        // 在后台线程中执行数据库查询
        viewModel.getById(id).collect { result ->
            savings = result
            isQueryFinished = true
        }
    }

    if (isQueryFinished) {
        var bankName by remember { mutableIntStateOf(savings.bankName ?: 0) }
        var name by remember { mutableIntStateOf(savings.name ?: 0) }
        var amount by remember { mutableStateOf(decimalFormat.format(savings.amount)) }
        var type by remember { mutableIntStateOf(savings.type ?: 0) }
        var startDate by remember { mutableStateOf(savings.startDate) }
        var dueDate by remember { mutableStateOf(savings.dueDate) }
        var rate by remember { mutableStateOf(decimalFormat.format(savings.rate)) }
        var income by remember { mutableDoubleStateOf(savings.income ?: 0.0) }
        var memo by remember { mutableStateOf(savings.memo) }

        CompositionLocalProvider(Constant.LocalSnackbarHostState provides snackbarHostState) {
            // 定义状态变量
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier.systemBarsPadding()
                ) {
                    TitleButton(
                        navController, "update", bankName, name,
                        amount, type, rate, startDate,
                        dueDate, income, memo ?: "", savings.uid
                    )
                    SnackbarHost(hostState = snackbarHostState)
                    Column(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                    ) {
                        ChoiceType(onTypeSelected = { selectedIndex ->
                            type = selectedIndex
                        }, savings.type ?: 0)
                        ChoiceBank(onTypeSelected = { selectedIndex ->
                            bankName = selectedIndex
                        }, savings.bankName ?: 0)
                        ChoiceName(onTypeSelected = { selectedIndex ->
                            name = selectedIndex
                        }, savings.name ?: 0)
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
                        income = showIncome(type, amount, rate, startDate, dueDate, income)

                        InputMemo(initValue = memo ?: "", onValueChange = { newText ->
                            memo = newText
                        })
                    }
                }
            }
        }
    }
}
