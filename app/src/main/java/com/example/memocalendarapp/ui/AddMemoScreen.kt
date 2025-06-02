package com.example.memocalendarapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Alignment
import com.example.memocalendarapp.data.Memo
import com.example.memocalendarapp.util.NotificationUtil
import androidx.compose.ui.unit.dp

// 深藍、日期色
val DeepBlue = Color(0xFF1F5673)
val DateGreyBlue = Color(0xFF6C7A89)

fun isValidTime24(time: String): Boolean {
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
    val focusManager = LocalFocusManager.current

    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        // 主標題＋日期
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "新增今日備忘錄",
                style = MaterialTheme.typography.titleLarge,
                color = DeepBlue
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "（$today）",
                style = MaterialTheme.typography.bodyLarge,
                color = DateGreyBlue
            )
        }
        Spacer(Modifier.height(16.dp))
        // 時間
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            supportingText = {
                if (showError) {
                    Text(errorMsg, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        // 地點
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("地點") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            singleLine = true
        )
        // 標題
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("標題") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            singleLine = true
        )
        // 說明
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("說明") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        Row(Modifier.padding(top = 16.dp)) {
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
                        focusManager.clearFocus()
                        onBack()
                    } catch (e: Exception) {
                        showError = true
                        errorMsg = "發生錯誤：${e.message}"
                        e.printStackTrace()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepBlue // 儲存按鈕底色深藍
                )
            ) { Text("儲存", color = Color.White) }
            Spacer(Modifier.width(16.dp))
            OutlinedButton(
                onClick = {
                    focusManager.clearFocus()
                    onBack()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DeepBlue // 取消按鈕文字顏色深藍
                )
            ) { Text("取消") }
        }
    }
}
