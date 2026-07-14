package com.example

import com.example.BuildConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import kotlinx.serialization.json.Json

@Serializable
data class GenerateContentRequest(val contents: List<Content>)
@Serializable
data class Content(val parts: List<Part>)
@Serializable
data class Part(val text: String)
@Serializable
data class GenerateContentResponse(val candidates: List<Candidate>)
@Serializable
data class Candidate(val content: Content)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

class GeminiRepository {
    private val service: GeminiApiService by lazy {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(GeminiApiService::class.java)
    }

    suspend fun generateContent(prompt: String): String {
        val request = GenerateContentRequest(listOf(Content(listOf(Part(prompt)))))
        return try {
            val response = service.generateContent(BuildConfig.GEMINI_API_KEY, request)
            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}
