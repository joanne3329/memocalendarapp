package com.example.memocalendarapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memocalendarapp.data.MemoDatabase
import com.example.memocalendarapp.viewmodel.MemoViewModel
import com.example.memocalendarapp.viewmodel.MemoViewModelFactory
import com.example.memocalendarapp.ui.MainScreen
import com.example.memocalendarapp.ui.AddMemoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = MemoDatabase.getDatabase(this)
        val factory = MemoViewModelFactory(db.memoDao())

        setContent {
            val context = LocalContext.current
            val viewModel: MemoViewModel = viewModel(factory = factory)
            var currentScreen by remember { mutableStateOf("main") }

            // 注意：此區塊只能呼叫 Composable function
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
