package com.example.memocalendarapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.memocalendarapp.R

class MemoReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "備忘提醒"
        val description = intent.getStringExtra("description") ?: ""
        val builder = NotificationCompat.Builder(context, "memo_reminder")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 你可換成自己的通知 icon
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}
