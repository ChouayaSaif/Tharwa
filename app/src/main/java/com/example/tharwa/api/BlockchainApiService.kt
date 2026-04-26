package com.example.tharwa.api

import kotlinx.coroutines.delay
import java.util.*

data class BlockchainResponse(
    val txHash: String,
    val status: String,
    val timestamp: Long
)

interface BlockchainApiService {
    suspend fun registerBatch(
        batchId: String,
        cropType: String,
        origin: String,
        certification: String
    ): BlockchainResponse
}

class MockBlockchainApiService : BlockchainApiService {
    override suspend fun registerBatch(
        batchId: String,
        cropType: String,
        origin: String,
        certification: String
    ): BlockchainResponse {
        delay(2000) // Simulate blockchain processing time
        return BlockchainResponse(
            txHash = "0x" + UUID.randomUUID().toString().replace("-", ""),
            status = "Verified",
            timestamp = System.currentTimeMillis()
        )
    }
}
