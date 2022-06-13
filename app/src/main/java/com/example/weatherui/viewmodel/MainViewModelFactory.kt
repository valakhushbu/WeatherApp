package com.example.weatherui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherui.repo.Repository

class MainViewModelFactory(private val repoHelper: Repository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repoHelper) as T
    }
}