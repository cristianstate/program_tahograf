package com.fitness.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.myapplication.data.TruckerLog
import com.fitness.myapplication.data.TruckerLogDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TruckerLogViewModel(private val truckerLogDao: TruckerLogDao) : ViewModel() {

    private val _searchQuery = kotlinx.coroutines.flow.MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val allLogs: StateFlow<List<TruckerLog>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                truckerLogDao.getAllLogs()
            } else {
                truckerLogDao.searchLogsByDate(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun insertLog(log: TruckerLog) {
        viewModelScope.launch {
            truckerLogDao.insert(log)
        }
    }

    fun updateLog(log: TruckerLog) {
        viewModelScope.launch {
            truckerLogDao.update(log)
        }
    }

    fun deleteLog(log: TruckerLog) {
        viewModelScope.launch {
            truckerLogDao.delete(log)
        }
    }

    suspend fun getLogById(id: Int): TruckerLog? {
        return truckerLogDao.getLogById(id)
    }
}
