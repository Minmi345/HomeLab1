package com.example.homelab1.model

import kotlinx.coroutines.Dispatchers
import android.content.Context
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import com.example.homelab1.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
interface GitHubApiService {
    @GET("repos/{owner}/{repo}/issues/{issue_number}/comments")
    suspend fun getComments(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/vnd.github+json",
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issue_number") issueNumber: Int
    ): List<GitHubComment>
}
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

    private val api: GitHubApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }

    suspend fun fetchGitHubComments(owner: String, repo: String, prNumber: Int): String {
        val fullUrl = "https://api.github.com/repos/$owner/$repo/issues/$prNumber/comments"
        return try {
            val token = BuildConfig.GITHUB_TOKEN
            val authHeader = "Bearer $token"

            val comments = api.getComments(
                authorization = authHeader,
                owner = owner,
                repo = repo,
                issueNumber = prNumber
            )

            if (comments.isNotEmpty()) {
                val latest = comments.last()
                "Fetched ${comments.size} comments! Latest by ${latest.user.username}: \"${latest.body}\""
            } else {
                "No comments found on PR #$prNumber"
            }
        } catch (e: Exception) {
            "ERROR [${e.javaClass.simpleName}]: ${e.localizedMessage}; url:$fullUrl"
        }
    }


}