package com.example.memocalendarapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.memocalendarapp.data.Memo
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddMemoScreen(
    today: String,
    onSave: (Memo) -> Unit,
    onBack: () -> Unit
) {
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp)) {
        Text("新增今日備忘錄（$today）", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("時間 (HH:mm)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
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
        if (showError) Text("標題必填且時間格式需正確", color = MaterialTheme.colorScheme.error)
        Row(Modifier.padding(top = 12.dp)) {
            Button(
                onClick = {
                    if (title.isBlank() || !time.matches(Regex("\\d{2}:\\d{2}"))) {
                        showError = true
                    } else {
                        onSave(
                            Memo(
                                date = today,
                                time = time,
                                location = location,
                                title = title,
                                description = description
                            )
                        )
                        onBack()
                    }
                }
            ) { Text("儲存") }
            Spacer(Modifier.width(16.dp))
            OutlinedButton(onClick = onBack) { Text("取消") }
        }
    }
}
