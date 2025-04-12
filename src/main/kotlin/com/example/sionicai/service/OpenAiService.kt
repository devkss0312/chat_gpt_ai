package com.example.sionicai.chat

import com.example.sionicai.dto.ChatCompletionRequest
import com.example.sionicai.dto.ChatCompletionResponse
import com.example.sionicai.dto.Message
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OpenAiService(
        @Value("\${openai.api.key}") private val apiKey: String
) {
    private val client = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    fun ask(messages: List<Message>, model: String = "gpt-3.5-turbo", stream: Boolean = false): String {
        val requestBody = ChatCompletionRequest(
                model = model,
                messages = messages,
                stream = stream
        )

        val body = objectMapper.writeValueAsString(requestBody)

        val request = Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create("application/json".toMediaTypeOrNull(), body))
                .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("OpenAI API error: ${response.code} - ${response.body?.string()}")
            }
            val json = response.body?.string() ?: throw RuntimeException("Empty response")
            val completion: ChatCompletionResponse = objectMapper.readValue(json)
            return completion.choices.firstOrNull()?.message?.content ?: "(No response)"
        }
    }
}
