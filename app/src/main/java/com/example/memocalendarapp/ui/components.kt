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


@Composable
fun MemoItem(
    memo: Memo,
    onDelete: () -> Unit,
    onOpenLocation: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 時間：大字、加粗
            Text(
                text = memo.time,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column(Modifier.weight(1f)) {
                // 標題：加粗
                Text(
                    memo.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                // 說明
                if (memo.description.isNotBlank()) {
                    Text(
                        memo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        maxLines = 2
                    )
                }
                // 地點：深色
                if (memo.location.isNotBlank()) {
                    Text(
                        "地點：${memo.location}",
                        color = MaterialTheme.colorScheme.primary,
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