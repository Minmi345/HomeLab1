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

    fun loadMockData() {
        viewModelScope.launch {
            val result = networkClient.fetchAndParseConfig()
            _uiState.value = result
        }
    }

    fun loadData() {
        viewModelScope.launch {
            try{
            val result = networkClient.fetchGitHubComments(
                owner = "Minmi345",
                repo = "cool_IoT_Simulator",
                prNumber = 2
            )
            _uiState.value = result}
            catch (e: Exception) {
                _uiState.value = "UI ERROR: ${e.localizedMessage}"
            }

        }
    }
}

