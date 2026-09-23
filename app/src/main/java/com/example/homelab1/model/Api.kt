package com.example.homelab1.model

import kotlinx.coroutines.Dispatchers
import android.content.Context
import kotlinx.coroutines.withContext
import com.google.gson.Gson

class NetworkClient (private val context: Context) {
    private val gson = Gson()
    suspend fun fetchData(): String {
        return withContext(Dispatchers.IO) {
            // Simulated network fetch
            "Data fetched successfully!"
        }
    }

    suspend fun fetchAndParseConfig(): String {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Read JSON file from assets
                val jsonString = context.assets.open("mock.json")
                    .bufferedReader()
                    .use { it.readText() }

                // 2. Parse JSON string into Kotlin Data Class
                val config = gson.fromJson(jsonString, HouseConfig::class.java)
//                config
                "SUCCESS: Temp is ${config.targetTemperature}°C by ${config.lastUpdatedBy}"
            } catch (e: Exception) {
                "ERROR [${e.javaClass.simpleName}]: ${e.localizedMessage}"
            }
        }
    }

}