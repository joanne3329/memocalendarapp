package com.example.memocalendarapp.util

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.memocalendarapp.R

/**
 * BroadcastReceiver 負責接收排程鬧鐘的廣播，並顯示通知。
 */
class MemoReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 日誌追蹤：確認已收到鬧鐘通知
        Log.d("AlarmDebug", "收到鬧鐘通知 onReceive")

        // 若手機 Android 版本 >= Android 13 (T)，需先檢查是否已取得 POST_NOTIFICATIONS 權限
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            // 若尚未授予通知權限，則不發送通知並直接返回
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        // 從 Intent 中取得通知標題與描述，若未提供則使用預設值
        val title = intent.getStringExtra("title") ?: "備忘提醒"
        val description = intent.getStringExtra("description") ?: ""

        // 建立通知樣板
        val builder = NotificationCompat.Builder(context, "memo_reminder")
            // 設定小圖示，會顯示在狀態列上
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            // 設定通知標題
            .setContentTitle(title)
            // 設定通知內容文字
            .setContentText(description)
            // 設定通知優先級為高，確保能以頭條方式顯示
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // 使用者點擊通知後自動移除通知
            .setAutoCancel(true)

        // 使用 NotificationManagerCompat 發送通知
        with(NotificationManagerCompat.from(context)) {
            // 以系統當前時間（毫秒）轉 int 作為通知 ID，避免重複
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}
