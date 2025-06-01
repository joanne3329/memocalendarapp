package com.example.memocalendarapp.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.memocalendarapp.data.Memo
import java.text.SimpleDateFormat
import java.util.*

object NotificationUtil {
    fun scheduleMemoReminder(context: Context, memo: Memo) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val dateTime = "${memo.date} ${memo.time}"
        val triggerAtMillis = try {
            sdf.parse(dateTime)?.time ?: return
        } catch (e: Exception) { return }

        val intent = Intent(context, MemoReminderReceiver::class.java).apply {
            putExtra("title", memo.title)
            putExtra("description", memo.description)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            memo.id, // 每個備忘錄一個 id
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }
}
