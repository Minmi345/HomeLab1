package com.example.homelab1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.homelab1.model.NetworkClient
import com.example.homelab1.ui.CommentUiState
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val networkClient = NetworkClient(application.applicationContext)
    private val _uiState = MutableStateFlow<CommentUiState>(CommentUiState.Loading)
    val uiState: StateFlow<CommentUiState> = _uiState

    fun loadMockData() {
        viewModelScope.launch {
            val result = networkClient.fetchAndParseConfig()
            _uiState.value = CommentUiState.Loading
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
                 if (isSuspicious(result)) {
                        _uiState.value = CommentUiState.Suspicious(result)
                    }
                 else {
                        _uiState.value = CommentUiState.Safe(result)
                    }
            }
            catch (e: Exception) {
                    _uiState.value = CommentUiState.Suspicious("UI ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun startPolling(){
      //  while (true) {
        viewModelScope.launch {
            try {
                val result = networkClient.fetchGitHubComments(
                    owner = "Minmi345",
                    repo = "cool_IoT_Simulator",
                    prNumber = 2
                )

                if (isSuspicious(result)) {
                    _uiState.value = CommentUiState.Suspicious(result)
                } else {
                    _uiState.value = CommentUiState.Safe(result)
                }

            } catch (e: Exception) {
                _uiState.value = CommentUiState.Suspicious("UI ERROR: ${e.localizedMessage}")
            }
            delay(100.seconds)

        //}
        }
    }

    private fun isSuspicious(text: String): Boolean {
        return text.contains("\'")
    }
}

