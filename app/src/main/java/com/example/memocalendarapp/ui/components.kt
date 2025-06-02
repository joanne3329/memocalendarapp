package com.example.memocalendarapp.ui

import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.memocalendarapp.data.Memo
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color


@Composable
fun MemoItem(
    memo: Memo,
    onDelete: () -> Unit,
    onOpenLocation: () -> Unit
) {
    // 主卡片底色、字體顏色
    val cardColor = Color(0xFFD1CFE2) // 柔和紫
    val timeColor = Color(0xFF1F5673) // 深藍
    val titleColor = Color.Black
    val descriptionColor = Color(0xFF333333) // 深灰
    val locationColor = Color(0xFF1F5673)    // 深藍

    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            Modifier.padding(18.dp),
            verticalAlignment = Alignment.Top
        ) {
            // 時間
            Text(
                text = memo.time,
                style = MaterialTheme.typography.headlineMedium,
                color = timeColor,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(Modifier.weight(1f)) {
                // 標題
                Text(
                    memo.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = titleColor
                )
                // 說明
                if (memo.description.isNotBlank()) {
                    Text(
                        memo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = descriptionColor,
                        maxLines = 2
                    )
                }
                // 地點
                if (memo.location.isNotBlank()) {
                    Text(
                        "地點：${memo.location}",
                        color = locationColor,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable { onOpenLocation() }
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "刪除")
            }
        }
    }
}
