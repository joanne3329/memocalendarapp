package com.example.memocalendarapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val time: String,
    val location: String,
    val title: String,
    val description: String
)
