package com.example.asset.ui.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asset.R
import com.example.asset.constant.Constant
import com.example.asset.ui.components.NearestSavingsDialog
import com.example.asset.ui.theme.AssetTheme
import com.example.asset.ui.theme.Typography
import com.example.asset.util.LunarUtil
import com.example.asset.viewmodel.MainViewModel
import java.text.DecimalFormat

@Composable
fun HomeHead() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 0.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        UserInfo()
        Buttons()
        Amount()
    }
}

// 最上面的用户信息
@Composable
fun UserInfo() {
    // 获取日期的字符串
    val dayStr = LunarUtil.getDayStr()
    val viewModel = Constant.LocalSharedViewModel.current
    val nearestSavings by viewModel.nearestSavings.observeAsState(null)
    var showDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
        ) {
            IconButton(
                onClick = {
                    viewModel.isDark = !viewModel.isDark // 切换主题变量
                },
                modifier = Modifier.size(40.dp) // 根据需要调整大小
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                "欢迎回来",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                dayStr,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }

        BadgedBox(
            badge = {
                Badge(
                    modifier = Modifier
                        .size(12.dp)
                        .offset(x = 0.dp, y = (-5).dp)
                )
            }
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(40.dp)
            ) {
                IconButton(
                    onClick = {
                        showDialog = true
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.alert),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }

    if (showDialog) {
        if (nearestSavings == null) {
            // Log.d("Eric", "nearestSavings i")
        } else {
            NearestSavingsDialog(nearestSavings!!) {
                showDialog = false
            }
        }
    }
}

// 按钮区
@Composable
fun Buttons() {
    val viewModel = Constant.LocalSharedViewModel.current
    var index by remember { mutableIntStateOf(0) }
    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
        ) {
            Button(
                onClick = {
                    index = 0
                    viewModel.changeShowType("date")
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (index == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    contentColor = if (index == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("日期", style = MaterialTheme.typography.bodyMedium)
            }
            Button(
                onClick = {
                    index = 1
                    viewModel.changeShowType("type")
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (index == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    contentColor = if (index == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("类别", style = MaterialTheme.typography.bodyMedium)
            }
            Button(
                onClick = {
                    index = 2
                    viewModel.changeShowType("bankName")
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (index == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    contentColor = if (index == 2) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("银行", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

}

// 合计金额
@Composable
fun Amount() {
    val viewModel = Constant.LocalSharedViewModel.current
    val totalSavings by viewModel.totalSavings.observeAsState(0.0)
    val totalIncome by viewModel.totalIncome.observeAsState(0.0)
    val decimalFormat = DecimalFormat("#,##0.00")

    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 12.dp, bottom = 0.dp),
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = "总存款",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "¥${decimalFormat.format(totalSavings)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = "预期利息",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "¥${decimalFormat.format(totalIncome)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeHead() {
    AssetTheme {
        UserInfo()
    }
}