package com.example.asset.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.asset.ui.theme.AssetTheme

val cardsColor =
    listOf(Color(0xFFFF0000), Color(0xFF00FF00), Color(0xFF0000FF),Color(0xFFFFFF00),Color(0xFF00FFFF))

@Composable
fun HomeCard(){
    val cards = mutableListOf("AAA", "BBB", "CCC", "DDD", "EEE")
    val expandedStates = remember { mutableStateListOf(*Array(cards.size) { false }) }
    val topCardIndex = remember { mutableIntStateOf(-1) } // 新增一个状态来记录当前最上层卡片的索引

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val availableWidth = screenWidth - 40.dp // 减去左右边距
    val offsetX = ((availableWidth - 200.dp) / (cards.size -1))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(start = 20.dp, end = 20.dp)
    ) {
        cards.forEachIndexed { index, content ->
            val isExpanded = expandedStates[index]
            Card(
                colors = CardDefaults.cardColors(containerColor = cardsColor[index]),
                modifier = Modifier
                    .offset(x = offsetX * index)
                    .size(200.dp)
                    .graphicsLayer {
                        scaleX = if (isExpanded) 1.2f else 1f
                        scaleY = if (isExpanded) 1.2f else 1f
                        alpha = if (isExpanded) 1f else 0.8f
                    }
                    .clickable {
                        if (index != topCardIndex.intValue) {
                            // 重置所有卡片的展开状态
                            for (i in expandedStates.indices) {
                                if (i != index) {
                                    expandedStates[i] = false
                                }
                            }
                        }
                        if (index == topCardIndex.intValue) {
                            // 如果再次点击当前最上层的卡片，重置 topCardIndex
                            topCardIndex.intValue = -1
                        } else {
                            topCardIndex.intValue = index // 点击时更新最上层卡片的索引
                        }
                        expandedStates[index] = !isExpanded
                    }
                    .zIndex(if (index == topCardIndex.intValue) 1f else 0f) // 根据索引设置 zIndex
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = content, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeBack(){
    AssetTheme {
        HomeCard()
    }
}