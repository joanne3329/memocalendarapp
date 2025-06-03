package com.example.memocalendarapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.memocalendarapp.data.Memo
import com.example.memocalendarapp.viewmodel.MemoViewModel

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
                title = { Text("今日備忘錄 ${viewModel.getToday()}") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD1CFE2),
                    titleContentColor = Color(0xFF1F5673)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMemo,
                containerColor = Color(0xFF1F5673),
                contentColor = Color.White,
                modifier = Modifier.testTag("FAB_ADD_MEMO")  // ★ 為測試加的
            ) {
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
                    color = Color(0xFF6C7A89)
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
            if (showDeleteDialog.first && showDeleteDialog.second != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false to null },
                    containerColor = Color(0xFFD1CFE2),
                    title = { Text("確認刪除", color = Color(0xFF1F5673)) },
                    text = { Text("確定要刪除此備忘錄嗎？", color = Color.Black) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteMemo(showDeleteDialog.second!!)
                                showDeleteDialog = false to null
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF1F5673))
                        ) { Text("刪除") }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteDialog = false to null },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF1F5673))
                        ) { Text("取消") }
                    }
                )
            }
        }
    }
}
