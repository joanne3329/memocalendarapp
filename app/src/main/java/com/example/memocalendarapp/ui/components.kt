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

@Composable
fun MemoItem(
    memo: Memo,
    onDelete: () -> Unit,
    onOpenLocation: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("時間：${memo.time}")
                Text("標題：${memo.title}")
                Text("說明：${memo.description}")
                if (memo.location.isNotBlank()) {
                    Text(
                        "地點：${memo.location}",
                        color = MaterialTheme.colorScheme.primary, // <<<<<<< Material3
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
