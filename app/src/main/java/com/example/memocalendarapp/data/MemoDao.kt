package com.example.memocalendarapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {
    @Insert
    suspend fun insert(memo: Memo)

    @Delete
    suspend fun delete(memo: Memo)

    @Query("SELECT * FROM memo WHERE date = :today")
    fun getTodayMemos(today: String): Flow<List<Memo>>
}
