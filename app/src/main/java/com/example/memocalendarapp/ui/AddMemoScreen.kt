package com.example.memocalendarapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.memocalendarapp.data.Memo
import com.example.memocalendarapp.util.NotificationUtil

fun isValidTime24(time: String): Boolean {
    // 正則可過 00:00 ~ 23:59
    val regex = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
    return regex.matches(time)
}

@Composable
fun AddMemoScreen(
    today: String,
    onSave: (Memo) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text("新增今日備忘錄（$today）", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = time,
            onValueChange = {
                time = it
                if (!isValidTime24(it) && it.isNotEmpty()) {
                    showError = true
                    errorMsg = "請輸入 24 小時制時間格式（00:00~23:59）"
                } else {
                    showError = false
                }
            },
            label = { Text("時間 (00:00~23:59)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            singleLine = true,
            supportingText = {
                if (showError) {
                    Text(errorMsg, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("地點") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("標題") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("說明") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        Row(Modifier.padding(top = 12.dp)) {
            Button(
                onClick = {
                    try {
                        if (title.isBlank()) {
                            showError = true
                            errorMsg = "標題必填"
                            return@Button
                        }
                        if (!isValidTime24(time)) {
                            showError = true
                            errorMsg = "請輸入 24 小時制時間格式（00:00~23:59）"
                            return@Button
                        }
                        showError = false
                        val memo = Memo(
                            date = today,
                            time = time,
                            location = location,
                            title = title,
                            description = description
                        )
                        onSave(memo)
                        NotificationUtil.scheduleMemoReminder(context, memo)
                        onBack()
                    } catch (e: Exception) {
                        showError = true
                        errorMsg = "發生錯誤：${e.message}"
                        // 你可以用 Log.e 印出錯誤
                        e.printStackTrace()
                    }
                }
            ) { Text("儲存") }
            Spacer(Modifier.width(16.dp))
            OutlinedButton(onClick = onBack) { Text("取消") }
        }
    }
}
