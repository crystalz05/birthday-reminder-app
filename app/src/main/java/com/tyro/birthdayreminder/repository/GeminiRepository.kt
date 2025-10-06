package com.tyro.birthdayreminder.repository

import android.util.Log
import com.tyro.birthdayreminder.BuildConfig
import com.tyro.birthdayreminder.entity.WishItem
import javax.inject.Inject
import retrofit2.awaitResponse

class GeminiRepository @Inject constructor(
    private val api: GeminiApi
) {
    suspend fun generateBirthdayWishes(name: String, age: Int, interest: String?): List<WishItem> {
        val prompt = """
            Suggest 5 birthday gift items for a $age-year-old named $name 
            whose likes are ${interest ?: "not stated"}.
            Make it short. Do not add numbering or bullets. 
            Separate each suggestion with a # sign.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                mapOf("parts" to listOf(mapOf("text" to prompt)))
            )
        )

        val response = api.generateContent(BuildConfig.GEMINI_API_KEY, request).awaitResponse()

        Log.d("Gemini Repository", response.toString())
        if (!response.isSuccessful) {
            return listOf(WishItem(name = "Error: ${response.message()}"))
        }

        val body = response.body()

        // Collect all text parts from all candidates
        val rawText = body?.candidates
            ?.flatMap { it.content.parts }
            ?.joinToString(" ") { it.text.orEmpty() }
            ?: ""

        // Convert to WishItem list
        return rawText
            .split("#")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { WishItem(name = it, selected = false) }
    }
}
