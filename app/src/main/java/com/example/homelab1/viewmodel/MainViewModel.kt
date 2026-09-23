package com.example.homelab1.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.homelab1.model.NetworkClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val networkClient = NetworkClient(application.applicationContext)
    private val _uiState = MutableStateFlow("Click to get info")
    val uiState: StateFlow<String> = _uiState

    fun loadData() {
        viewModelScope.launch {
            val result = networkClient.fetchAndParseConfig()
            _uiState.value = result
        }
    }
}

