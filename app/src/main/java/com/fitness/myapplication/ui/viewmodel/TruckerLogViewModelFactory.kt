package com.fitness.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fitness.myapplication.data.TruckerLogDao

class TruckerLogViewModelFactory(private val truckerLogDao: TruckerLogDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TruckerLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TruckerLogViewModel(truckerLogDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
