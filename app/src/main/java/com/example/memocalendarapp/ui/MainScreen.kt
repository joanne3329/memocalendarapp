package com.example.memocalendarapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.memocalendarapp.data.Memo
import com.example.memocalendarapp.viewmodel.MemoViewModel
// 不需要 import components.MemoItem，因為就在同 package

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MemoViewModel,
    onAddMemo: () -> Unit,
    onOpenLocation: (String) -> Unit
) {
    val memos by viewModel.todayMemos.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<Pair<Boolean, Memo?>>(false to null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("今日備忘錄 ${viewModel.getToday()}") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMemo) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            if (memos.isEmpty()) {
                Text(
                    "今天沒有備忘錄喔！",
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(24.dp),
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                LazyColumn {
                    items(memos.size) { index ->
                        val memo = memos[index]
                        MemoItem(
                            memo = memo,
                            onDelete = { showDeleteDialog = true to memo },
                            onOpenLocation = { onOpenLocation(memo.location) }
                        )
                    }
                }
            }
            // 刪除確認 Dialog
            if (showDeleteDialog.first && showDeleteDialog.second != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false to null },
                    title = { Text("確認刪除") },
                    text = { Text("確定要刪除此備忘錄嗎？") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteMemo(showDeleteDialog.second!!)
                            showDeleteDialog = false to null
                        }) { Text("刪除") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false to null }) { Text("取消") }
                    }
                )
            }
        }
    }
}
