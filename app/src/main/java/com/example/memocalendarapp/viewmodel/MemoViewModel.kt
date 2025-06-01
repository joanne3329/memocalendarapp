package com.example.memocalendarapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.memocalendarapp.data.Memo
import com.example.memocalendarapp.data.MemoDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MemoViewModel(private val dao: MemoDao) : ViewModel() {
    private val today: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val todayMemos: StateFlow<List<Memo>> =
        dao.getTodayMemos(today).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertMemo(memo: Memo) = viewModelScope.launch {
        dao.insert(memo)
    }

    fun deleteMemo(memo: Memo) = viewModelScope.launch {
        dao.delete(memo)
    }

    fun getToday(): String = today
}

class MemoViewModelFactory(private val dao: MemoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MemoViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
