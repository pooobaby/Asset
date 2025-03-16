package com.example.asset.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.asset.constant.Constant
import com.example.asset.data.local.entity.Savings
import com.example.asset.util.CalcUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 选择类型
@Composable
fun ChoiceType(onTypeSelected: (Int) -> Unit, typeIndex: Int) {
    var selectedIndex by remember { mutableIntStateOf(typeIndex) }
    val options = Constant.TYPE_LIST

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                colors = SegmentedButtonDefaults.colors(
                    inactiveContainerColor = MaterialTheme.colorScheme.secondary,
                    activeContainerColor = MaterialTheme.colorScheme.primary,
                    activeContentColor = MaterialTheme.colorScheme.onPrimary,
                    inactiveContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    selectedIndex = index
                    onTypeSelected(selectedIndex)
                },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

// 选择银行
@Composable
fun ChoiceBank(onTypeSelected: (Int) -> Unit, bankNameIndex: Int) {
    // 定义菜单的展开状态
    var expanded by remember { mutableStateOf(false) }
    // 定义选项列表
    val options = Constant.BANK_LIST
    // 定义当前选中的选项
    var selectedOption by remember { mutableStateOf(options[bankNameIndex]) }
    // 定义当前选中的选项索引
    var selectedIndex by remember { mutableIntStateOf(bankNameIndex) }

    Row(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "银行：",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        // 触发按钮
        TextButton(
            onClick = { expanded = true },
            // 消除TextButton的内部间距
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.height(24.dp)
        ) {
            Text(
                text = selectedOption,
                style = MaterialTheme.typography.bodyMedium,
            )
            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expand")
        }

        // 下拉菜单
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        selectedIndex = index
                        onTypeSelected(selectedIndex)
                        expanded = false
                    },
                    enabled = true,
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }
    }
}

// 选择姓名
@Composable
fun ChoiceName(onTypeSelected: (Int) -> Unit, nameIndex: Int) {
    // 定义菜单的展开状态
    var expanded by remember { mutableStateOf(false) }
    // 定义选项列表
    val options = Constant.NAME_LIST
    // 定义当前选中的选项
    var selectedOption by remember { mutableStateOf(options[nameIndex]) }
    // 定义当前选中的选项索引
    var selectedIndex by remember { mutableIntStateOf(nameIndex) }

    Row(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "人员：",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        // 触发按钮
        TextButton(
            onClick = { expanded = true },
            // 消除TextButton的内部间距
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.height(24.dp)
        ) {
            Text(
                text = selectedOption,
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expand")
        }

        // 下拉菜单
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        selectedIndex = index
                        onTypeSelected(selectedIndex)
                        expanded = false
                    },
                    enabled = true,
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }
    }
}

// 输入金额
@Composable
fun InputSaving(initValue: String, onValueChange: (String) -> Unit) {
    val cleanValue = initValue.replace(",", "")     // 去掉 initValue 中的千分符
    var isFocused by remember { mutableStateOf(false) } // 跟踪焦点状态
    var formattedValue by remember { mutableStateOf(initValue) }
    var noFormattedValue by remember { mutableStateOf(cleanValue) }

    if (isFocused && initValue == "0.00") {
        onValueChange("")
        noFormattedValue = ""
    }

    BasicTextField(
        value = if (isFocused) noFormattedValue else formattedValue,
        onValueChange = { newText ->
            if (newText.isNotEmpty() && newText.matches(Regex("^(0|[1-9]\\d*)\\.?\\d{0,2}$"))) {
                onValueChange(newText)
                noFormattedValue = newText
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        ),
        modifier = Modifier
            .focusable(enabled = true)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (!isFocused) {
                    try {
                        val number = noFormattedValue.toDouble()
                        // 使用 DecimalFormat 格式化数字，保留两位小数
                        val decimalFormat = DecimalFormat("#,##0.00")
                        val formatted = decimalFormat.format(number)
                        formattedValue = formatted
                        onValueChange(formatted)
                    } catch (e: NumberFormatException) {
                        // TODO 如果输入不是有效的数字，不进行格式化
                    }
                }
            }
            .padding(start = 12.dp, end = 12.dp, top = 12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "金额：",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .background(
                            if (isFocused)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.background
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(start = 12.dp, end = 12.dp),  // 这里调整的是内边距
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¥",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        innerTextField()
                    }

                }
            }
        },
    )
}

// 输入利率
@Composable
fun InputRate(initValue: String, onValueChange: (String) -> Unit) {
    val cleanValue = initValue.replace(",", "")     // 去掉 initValue 中的千分符
    var isFocused by remember { mutableStateOf(false) } // 跟踪焦点状态
    var formattedValue by remember { mutableStateOf(initValue) }
    var noFormattedValue by remember { mutableStateOf(cleanValue) }


    if (isFocused && initValue == "0.00") {
        onValueChange("")
        noFormattedValue = ""
    }

    BasicTextField(
        value = if (isFocused) noFormattedValue else formattedValue,
        onValueChange = { newText ->
            if (newText.isNotEmpty() && newText.matches(Regex("^(0|[1-9]\\d*)\\.?\\d{0,2}$"))) {
                onValueChange(newText)
                noFormattedValue = newText
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        ),
        singleLine = true,
        modifier = Modifier
            .focusable(enabled = true)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (!isFocused) {
                    try {
                        val number = noFormattedValue.toDouble()
                        // 使用 DecimalFormat 格式化数字，保留两位小数
                        val decimalFormat = DecimalFormat("#,##0.00")
                        val formatted = decimalFormat.format(number)
                        formattedValue = formatted
                        onValueChange(formatted)
                    } catch (e: NumberFormatException) {
                        // TODO 如果输入不是有效的数字，不进行格式化
                    }
                }
            }
            .padding(start = 12.dp, end = 12.dp, top = 12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "利率：",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .background(
                            if (isFocused)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.background
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(8.dp)
                        )
                        .width(100.dp)
                        .padding(start = 12.dp, end = 12.dp),  // 这里调整的是内边距
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    innerTextField()
                }
                Text(
                    text = "%",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        },
    )
}

// 选择起存日
@Composable
fun ChoiceStartDate(
    initStartDate: Long?,
    onStartDateChanged: (Long?) -> Unit
) {
    // 控制弹窗的显示和隐藏
    var showDatePicker by remember { mutableStateOf(false) }
    // 存储用户选择的日期
    var selectedDate by remember { mutableStateOf(initStartDate) }

    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date())
    val dateStr: String
    // 显示选择的日期
    if (selectedDate != null) {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(selectedDate!!))
        dateStr = formattedDate
    } else {
        dateStr = todayDate
    }

    Row(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "起存日：",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = dateStr,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(bottom = 0.dp)
        )

        Spacer(Modifier.width(20.dp))
        // 打开日期选择器的按钮
        IconButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(Icons.Default.DateRange, null,
                tint = MaterialTheme.colorScheme.onBackground)
        }
    }


    // 显示日期选择器弹窗
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { dateMillis ->
                onStartDateChanged(dateMillis) // 更新 startDate
                selectedDate = dateMillis
            },
            onDismiss = { showDatePicker = false },
            initStartDate
        )
    }
}

// 选择到期日
@Composable
fun ChoiceDueDate(
    initDueDate: Long?,
    onStartDateChanged: (Long?) -> Unit
) {
    // 控制弹窗的显示和隐藏
    var showDatePicker by remember { mutableStateOf(false) }
    // 存储用户选择的日期
    var selectedDate by remember { mutableStateOf(initDueDate) }

    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date())
    val dateStr: String
    // 显示选择的日期
    if (selectedDate != null) {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(selectedDate!!))
        dateStr = formattedDate
    } else {
        dateStr = todayDate
    }

    Row(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "到期日：",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = dateStr,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.width(20.dp))

        // 打开日期选择器的按钮
        IconButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(Icons.Default.DateRange, null,
                tint = MaterialTheme.colorScheme.onBackground)
        }
    }
    // 显示日期选择器弹窗
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { dateMillis ->
                selectedDate = dateMillis
                onStartDateChanged(dateMillis)
            },
            onDismiss = { showDatePicker = false },
            initDueDate
        )
    }
}

// 显示收益
@Composable
fun showIncome(
    type: Int,
    amount: String,
    rate: String,
    startDate: Long?,
    dueDate: Long?,
    initIncome: Double
): Double {
    val decimalFormat = DecimalFormat("#,##0.00")
    var incomeStr by remember { mutableStateOf(decimalFormat.format(initIncome)) }
    var calculatedIncome = 0.0
    // 尝试进行计算
    if (startDate != null && dueDate != null) {
        try {
            // 对数据先进行清洗
            val cleanAmount: Double
            val cleanRate: Double
            val cleanAmountStr = amount.replace(",", "")
            val cleanRateStr = rate.replace(",", "")
            cleanAmount = if (cleanAmountStr.isEmpty())  0.0 else cleanAmountStr.toDouble()
            cleanRate = if (cleanRateStr.isEmpty())  0.0 else cleanRateStr.toDouble()
            // 计算收益
            calculatedIncome = CalcUtil.calcIncome(type, cleanAmount, cleanRate, startDate, dueDate)
            incomeStr = decimalFormat.format(calculatedIncome)
        } catch (e: NumberFormatException) {
            Log.d("Eric", "数据格式转换异常：$e")
            incomeStr = "0.00"
        }
    }

    Row(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "预期收益：",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = incomeStr,
            color = MaterialTheme.colorScheme.onError,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    return calculatedIncome
}

// 输入备注
@Composable
fun InputMemo(initValue: String, onValueChange: (String) -> Unit) {
    var isFocused by remember { mutableStateOf(false) } // 跟踪焦点状态

    BasicTextField(
        value = initValue,
        onValueChange = { newText ->
            onValueChange(newText)
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
        ),
        modifier = Modifier
            .focusable(enabled = true)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .padding(start = 12.dp, end = 12.dp, top = 12.dp),
        maxLines = 3,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "备注：",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .heightIn(min = 36.dp)
                        .background(
                            if (isFocused)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.background
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth()
                        .padding(top = 6.dp, start = 12.dp, end = 12.dp),  // 这里调整的是内边距
                ) {
                    innerTextField()
                }
            }
        },
    )
}

// 日期选择器
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    initialDate:Long? = null
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(
                    text = "确定",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "取消",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondary // 修改背景颜色
        ),
        modifier = Modifier.graphicsLayer(scaleX = 0.8f, scaleY = 0.8f) // 添加缩放效果到 DatePickerDialog
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(scaleX = 0.9f, scaleY = 0.9f) // 缩放比例
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondary, // 修改背景颜色
                    titleContentColor = MaterialTheme.colorScheme.onBackground, // 修改标题颜色
                    weekdayContentColor = MaterialTheme.colorScheme.onBackground, // 修改星期几颜色
                    dayContentColor = MaterialTheme.colorScheme.onBackground, // 修改日期颜色
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary, // 修改选中日期背景颜色
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary, // 修改选中日期文字颜色
                    todayContentColor = MaterialTheme.colorScheme.primary, // 修改今天日期颜色
                ),
            )
        }
    }
}

@Composable
fun TitleButton(
    navController: NavController, option: String,
    bankName: Int, name: Int, amount: String, type: Int, rate: String,
    startDate: Long?, dueDate: Long?, income: Double, memo: String, id: Int
) {
    // 通过 CompositionLocal 获取 ViewModel
    val viewModel = Constant.LocalSharedViewModel.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = Constant.LocalSnackbarHostState.current

    Row(
        Modifier.padding(start = 6.dp, end = 6.dp)
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.onBackground)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            if (option == "append") {
                try {
                    val cleanAmount = amount.replace(",", "")
                    val cleanRate = rate.replace(",", "")
                    val appendSavings = Savings(
                        uid = 0,
                        bankName = bankName,
                        name = name,
                        amount = cleanAmount.toDouble(),
                        type = type,
                        rate = cleanRate.toDouble(),
                        startDate = startDate,
                        dueDate = dueDate,
                        income = income,
                        memo = memo
                    )
                    viewModel.insert(appendSavings)
                    // 可以在这里进行日志记录或者其他操作
                    Log.d("Eric", "添加存款成功")
                    // 显示 Snackbar
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "添加存款成功",
                            duration = SnackbarDuration.Short)
                    }
                    scope.launch {
                        delay(1000)
                        navController.popBackStack()
                    }
                } catch (e: Exception) {
                    // 处理异常
                    e.printStackTrace()
                }
            } else if (option == "update") {
                Log.d("Eric", "开始修改存款")
                try {
                    val cleanAmount = amount.replace(",", "")
                    val cleanRate = rate.replace(",", "")
                    val updateSavings = Savings(
                        uid = id,
                        bankName = bankName,
                        name = name,
                        amount = cleanAmount.toDouble(),
                        type = type,
                        rate = cleanRate.toDouble(),
                        startDate = startDate,
                        dueDate = dueDate,
                        income = income,
                        memo = memo
                    )
                    viewModel.update(updateSavings)
                    // 可以在这里进行日志记录或者其他操作
                    Log.d("Eric", "修改存款成功")
                    // 显示 Snackbar
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "修改存款成功",
                            duration = SnackbarDuration.Short)
                    }
                    scope.launch {
                        delay(1000)
                        navController.popBackStack()
                    }
                } catch (e: Exception) {
                    // 处理异常
                    e.printStackTrace()
                }
            }

        }) {
            Icon(Icons.Default.Done, null, tint = MaterialTheme.colorScheme.onBackground)
        }
    }

}
