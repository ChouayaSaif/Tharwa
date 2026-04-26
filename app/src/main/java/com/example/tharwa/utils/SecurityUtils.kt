package com.example.tharwa.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {

    private const val AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val AES_ALGORITHM = "AES"
    private const val HMAC_ALGORITHM = "HmacSHA256"
    private val SECRET_KEY = "TharwaSecureKey123".substring(0, 16) // 16 bytes for AES-128
    private val IV = "TharwaInitVector".substring(0, 16).toByteArray() // Fixed IV for simplicity in demo

    // 1. AES Encryption
    fun encrypt(data: String): String {
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), AES_ALGORITHM)
        val ivSpec = IvParameterSpec(IV)
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encryptedData: String): String {
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), AES_ALGORITHM)
        val ivSpec = IvParameterSpec(IV)
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val decodedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
        return String(cipher.doFinal(decodedBytes))
    }

    // 2. Input Sanitization
    fun sanitizeInput(input: String): String {
        // Remove HTML tags
        var sanitized = input.replace(Regex("<[^>]*>"), "")
        // Limit length
        sanitized = sanitized.take(100)
        // Safe characters only (alphanumeric and spaces)
        return sanitized.filter { it.isLetterOrDigit() || it.isWhitespace() }
    }

    // 3. Prompt Guard
    fun isSafePrompt(prompt: String): Boolean {
        val unsafeKeywords = listOf("ignore previous", "override", "hack", "system", "delete")
        return unsafeKeywords.none { prompt.lowercase().contains(it) }
    }

    // 4. HMAC Signing for QR
    fun signBatchId(batchId: String): String {
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), HMAC_ALGORITHM)
        val mac = Mac.getInstance(HMAC_ALGORITHM)
        mac.init(keySpec)
        val signatureBytes = mac.doFinal(batchId.toByteArray())
        return Base64.encodeToString(signatureBytes, Base64.NO_WRAP)
    }

    fun verifyBatchId(batchId: String, signature: String): Boolean {
        return signBatchId(batchId) == signature
    }
}
