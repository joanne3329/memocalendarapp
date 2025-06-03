package com.example.memocalendarapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memocalendarapp.data.MemoDatabase
import com.example.memocalendarapp.ui.AddMemoScreen
import com.example.memocalendarapp.ui.MainScreen
import com.example.memocalendarapp.ui.theme.MemoAppTheme
import com.example.memocalendarapp.viewmodel.MemoViewModel
import com.example.memocalendarapp.viewmodel.MemoViewModelFactory

class MainActivity : ComponentActivity() {

    // 使用 registerForActivityResult 以請求通知權限
    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // 依據用戶回應提供適當提示
        if (isGranted) {
            Toast.makeText(this, "已開啟通知權限", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "未開啟通知權限，將無法收到提醒！", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 建立通知頻道 (僅 Android Oreo 以上)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "memo_reminder", // 頻道識別碼
                "備忘提醒", // 頻道名稱
                android.app.NotificationManager.IMPORTANCE_HIGH // 重要性設為高
            )
            val manager = getSystemService(android.app.NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // Android 13 以上動態請求通知權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // 取得資料庫實例與 ViewModel 工廠
        val db = MemoDatabase.getDatabase(this)
        val factory = MemoViewModelFactory(db.memoDao())

        setContent {
            MemoAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    val context = LocalContext.current
                    val viewModel: MemoViewModel = viewModel(factory = factory)
                    var currentScreen by remember { mutableStateOf("main") }

                    // 根據當前畫面狀態，決定顯示畫面
                    when (currentScreen) {
                        "main" -> MainScreen(
                            viewModel = viewModel,
                            onAddMemo = { currentScreen = "add" },
                            onOpenLocation = { location ->
                                val uri = Uri.parse("geo:0,0?q=${Uri.encode(location)}")
                                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                    setPackage("com.google.android.apps.maps")
                                }
                                context.startActivity(intent)
                            }
                        )
                        "add" -> AddMemoScreen(
                            today = viewModel.getToday(),
                            onSave = {
                                viewModel.insertMemo(it)
                                currentScreen = "main"
                            },
                            onBack = { currentScreen = "main" }
                        )
                    }
                }
            }
        }
    }
}