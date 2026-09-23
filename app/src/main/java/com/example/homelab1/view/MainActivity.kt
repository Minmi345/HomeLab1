package com.example.homelab1.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.homelab1.ui.theme.HomeLab1Theme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.homelab1.viewmodel.MainViewModel
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.homelab1.BuildConfig

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeLab1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = ":(",
                        modifier = Modifier.padding(innerPadding)
                    )
                    TokenDisplayScreen()
                    DataScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun TokenDisplayScreen() {
    val token = BuildConfig.GITHUB_TOKEN

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (token.isNotEmpty()) "Token: $token" else "Token Not Found in BuildConfig",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    HomeLab1Theme {
//        Greeting("Android")
//    }
//}

@Composable
fun DataScreen(viewModel: MainViewModel) {
    val textState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { viewModel.loadData() }) {
            Text(text = textState)
        }
    }
}