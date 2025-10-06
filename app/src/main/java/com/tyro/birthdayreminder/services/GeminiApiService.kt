package com.tyro.birthdayreminder.repository

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Call

// Request/response models
data class GeminiRequest(val contents: List<Map<String, List<Map<String, String>>>>)
data class GeminiResponse(val candidates: List<Candidate>)
data class Candidate(val content: Content)
data class Content(val parts: List<Part>)
data class Part(val text: String)

interface GeminiApi {
    @Headers("Content-Type: application/json")
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    fun generateContent(
        @Query("key") apiKey: String,
        @Body body: GeminiRequest
    ): Call<GeminiResponse>
}