package com.example.memocalendarapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memocalendarapp.data.MemoDatabase
import com.example.memocalendarapp.viewmodel.MemoViewModel
import com.example.memocalendarapp.viewmodel.MemoViewModelFactory
import com.example.memocalendarapp.ui.MainScreen
import com.example.memocalendarapp.ui.AddMemoScreen
import com.example.memocalendarapp.ui.theme.MemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ===== 建立通知頻道 =====
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "memo_reminder",
                "備忘提醒",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(android.app.NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // ===== Android 13+ 動態請求通知權限 =====
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        val db = MemoDatabase.getDatabase(this)
        val factory = MemoViewModelFactory(db.memoDao())

        setContent {
            MemoAppTheme { // 藍色主題
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding() // 不被相機孔壓到
                ) {
                    val context = LocalContext.current
                    val viewModel: MemoViewModel = viewModel(factory = factory)
                    var currentScreen by remember { mutableStateOf("main") }

                    when (currentScreen) {
                        "main" -> MainScreen(
                            viewModel = viewModel,
                            onAddMemo = { currentScreen = "add" },
                            onOpenLocation = { location ->
                                val uri = Uri.parse("geo:0,0?q=${Uri.encode(location)}")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                intent.setPackage("com.google.android.apps.maps")
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
