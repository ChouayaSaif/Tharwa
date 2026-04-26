package com.example.tharwa.api

import android.content.Context
import com.example.tharwa.utils.SecurityUtils
import kotlinx.coroutines.delay

class LocalAIAnalyzer(private val context: Context) {

    // Simulating Local TensorFlow Lite Inference
    suspend fun analyzeFoodLocally(input: String): FoodResponse {
        delay(1500) // Simulate processing time on device

        // 1. Sanitize Input
        val sanitized = SecurityUtils.sanitizeInput(input)

        // 2. Prompt Guard
        if (!SecurityUtils.isSafePrompt(sanitized)) {
            return FoodResponse("danger", "Security Alert: Unsafe input detected", "Instruction blocked by system")
        }

        // 3. Simulated AI Model Logic (Mocking model.tflite results)
        return when {
            sanitized.contains("peanut", ignoreCase = true) -> 
                FoodResponse("risky", "Local AI detected: Peanut trace", "Warning for allergic users")
            sanitized.contains("sugar", ignoreCase = true) && sanitized.contains("high", ignoreCase = true) ->
                FoodResponse("risky", "Local AI detected: High sugar content", "Avoid for diabetes profile")
            sanitized.isEmpty() ->
                FoodResponse("unknown", "Empty scan data", "Please scan a valid product")
            else ->
                FoodResponse("safe", "Local AI: No hazards found", "Safe to consume")
        }
    }
}
