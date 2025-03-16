package com.example.asset.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.asset.constant.Constant
import com.example.asset.data.local.entity.Savings
import com.example.asset.navigation.LocalNavController
import com.example.asset.ui.components.MiniButtonDialog
import com.example.asset.ui.theme.AssetTheme
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeList() {
    // 通过 CompositionLocal 获取 ViewModel
    val viewModel = Constant.LocalSharedViewModel.current
    val groupYear by viewModel.groupedSavings.observeAsState(emptyMap())

    // 添加 SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(Constant.LocalSnackbarHostState provides snackbarHostState) {
        Box(
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            if (groupYear.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "库中是空的",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(all = 12.dp)
                        .systemBarsPadding()
                ) {
                    // 对 groupYear 的键进行排序
                    groupYear.toSortedMap().forEach { (key, savingsList) ->
                        // 显示分组标题
                        item {
                            Text(
                                text = key.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // 显示该年份下的所有储蓄记录
                        items(savingsList) { savings ->
                            ItemView(savings)
                        }
                    }
                }
                SnackbarHost(hostState = snackbarHostState)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemView(savings: Savings) {
    val viewModel = Constant.LocalSharedViewModel.current
    val decimalFormat = DecimalFormat("#,##0.00")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    // 用于控制对话框的显示与隐藏
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = Constant.LocalSnackbarHostState.current

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.padding(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        showBottomSheet = true
                    },
                    onLongClick = {
                        // 长按触发显示对话框
                        showDialog = true
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .size(40.dp)
            ) {
                Text(
                    text = Constant.BANK_LIST[savings.bankName!!].firstOrNull().toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.wrapContentSize(Alignment.Center)
                )
            }
            // 显示储蓄的金额
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = decimalFormat.format(savings.amount),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 12.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Column(
                Modifier.padding(end = 5.dp)
            ) {
                Text(
                    text = "起：" + dateFormat.format(savings.startDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "止：" + dateFormat.format(savings.dueDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }

    if (showDialog) {
        val dueDateStr = dateFormat.format(savings.dueDate)
        val amountStr = decimalFormat.format(savings.amount)
        MiniButtonDialog(
            dueDate = dueDateStr,
            amount = amountStr,
            onDismissRequest = {
                showDialog = false
            },
            onConfirmation = {
                // 显示 Snackbar
                scope.launch {
                    showDialog = false
                    snackbarHostState.showSnackbar(
                        message = "正在删除存款...",
                        duration = SnackbarDuration.Short
                    )
                    viewModel.delete(savings)
                }
            }
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onBackground,
            shape = BottomSheetDefaults.HiddenShape,
            modifier = Modifier.systemBarsPadding()
        ) {
            ShowSavingsInfo(savings)
        }
    }
}

@Composable
fun ShowSavingsInfo(savings: Savings) {
    val navController = LocalNavController.current
    val decimalFormat = DecimalFormat("#,##0.00")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    Column(
        Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = decimalFormat.format(savings.amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {
                navController.navigate("update/${savings.uid}")
            }) {
                Icon(
                    Icons.Filled.Edit, null,
                    modifier = Modifier.wrapContentSize()
                )
            }
        }

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = Constant.TYPE_LIST[savings.type!!],
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "利率：",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 20.dp)
            )
            Text(
                text = "${decimalFormat.format(savings.rate)}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "预期收益：",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 20.dp)
            )
            Text(
                text = decimalFormat.format(savings.income),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Text(
                text = Constant.BANK_LIST[savings.bankName!!],
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = Constant.NAME_LIST[savings.name!!],
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Text(
                text = "起存日：",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = dateFormat.format(savings.startDate),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "到期日：",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 20.dp)
            )
            Text(
                text = dateFormat.format(savings.dueDate),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "备注：${savings.memo!!}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Preview
@Composable
fun PreviewHomeList() {
    val testSavings = Savings(
        uid = 0, bankName = 1, name = 0, amount = 250000.00,
        type = 0, startDate = 0, dueDate = 0, rate = 2.5, income = 5000.00, memo = "test"
    )
    AssetTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
        ) {
            ItemView(testSavings)
        }
    }
}