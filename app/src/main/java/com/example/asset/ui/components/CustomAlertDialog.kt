package com.example.asset.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.asset.constant.Constant
import com.example.asset.data.local.entity.Savings
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun StandardAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String? = null,
    dialogText: String,
    icon: ImageVector?,
) {
    AlertDialog(
        icon = {
            if (icon != null) {
                Icon(
                    icon, contentDescription = "Example Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        title = {
            if (dialogTitle != null) {
                Text(text = dialogTitle)
            }
        },
        containerColor = MaterialTheme.colorScheme.secondary,
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("确认")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("取消")
            }
        }
    )
}

@Composable
fun MiniButtonDialog(
    dueDate: String,
    amount: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)
                        .fillMaxWidth()
                ) {
                Text(
                    text = "金额：$amount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
                )
                Text(
                    text = "到期日：$dueDate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 6.dp, start = 20.dp, end = 20.dp)
                )
                Text(
                    text = "确定删除吗？操作不可恢复！",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(top = 6.dp, start = 20.dp, end = 20.dp, bottom = 6.dp)
                )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Row {
                        IconButton(
                            onClick = { onDismissRequest() },
                        ) {
                            Icon(
                                Icons.Default.Close,
                                null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                        IconButton(
                            onClick = { onConfirmation() },
                            modifier = Modifier.padding(start = 40.dp),
                        ) {
                            Icon(
                                Icons.Default.Done,
                                null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NearestSavingsDialog(
    savings: Savings,
    onDismissRequest: () -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val decimalFormat = DecimalFormat("#,##0.00")

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 0.dp, bottom = 20.dp, top = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(top = 0.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "${dateFormat.format(savings.dueDate)}将有一笔存款到期",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f).align(Alignment.Bottom)
                    )
                    IconButton(onClick = {
                        onDismissRequest()
                    },
                        modifier = Modifier
                            .padding(end = 0.dp)
                            .align(Alignment.Bottom)
                    ) {
                        Icon(Icons.Filled.Clear,null,
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text(
                        text = Constant.BANK_LIST[savings.bankName ?: 0],
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = Constant.TYPE_LIST[savings.type ?: 0],
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
                Text(
                    text = "金额：${decimalFormat.format(savings.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(
                    text = "预计收益：${decimalFormat.format(savings.income)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(
                    text = "起存日期：${dateFormat.format(savings.startDate)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 6.dp)
                )


            }
        }
    }
}

@Preview
@Composable
fun PreviewDialog() {
    val testSavings = Savings(
        uid = 0, bankName = 1, name = 0, amount = 250000.00,
        type = 0, startDate = 0, dueDate = 0, rate = 2.5, income = 5000.00, memo = "test"
    )
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
    ) {
        NearestSavingsDialog(testSavings) {

        }
    }
}

