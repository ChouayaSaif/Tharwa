package com.example.tharwa.repository

import com.example.tharwa.api.BlockchainApiService
import com.example.tharwa.api.FoodApiService
import com.example.tharwa.api.FoodResponse
import com.example.tharwa.api.MockBlockchainApiService
import com.example.tharwa.data.*
import com.example.tharwa.utils.SecurityUtils
import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppRepository(
    private val userDao: UserDao,
    private val foodEntryDao: FoodEntryDao,
    private val foodBatchDao: FoodBatchDao,
    private val marketplaceItemDao: MarketplaceItemDao,
    private val offerDao: OfferDao,
    private val blockchainService: BlockchainApiService = MockBlockchainApiService()
) {
    // 6. JWT Authentication (Mock) + User-Agent for Open Food Facts
    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer fake_jwt_token_tharwa_2024")
            // Open Food Facts requires a User-Agent to avoid 403 errors
            .addHeader("User-Agent", "TharwaApp - Android - Version 1.0 - https://github.com/example/tharwa")
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .build()

    private val apiService: FoodApiService = Retrofit.Builder()
        .baseUrl("https://world.openfoodfacts.org/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FoodApiService::class.java)

    suspend fun getUser(username: String): User? {
        val user = userDao.getUserByUsername(username)
        // 5. AES Encryption (Decrypting on fetch)
        return user?.copy(
            allergies = if (user.allergies.isNotEmpty()) SecurityUtils.decrypt(user.allergies) else "",
            avoidedIngredients = if (user.avoidedIngredients.isNotEmpty()) SecurityUtils.decrypt(user.avoidedIngredients) else ""
        )
    }

    suspend fun insertUser(user: User) {
        // 5. AES Encryption (Encrypting on save)
        val encryptedUser = user.copy(
            allergies = if (user.allergies.isNotEmpty()) SecurityUtils.encrypt(user.allergies) else "",
            avoidedIngredients = if (user.avoidedIngredients.isNotEmpty()) SecurityUtils.encrypt(user.avoidedIngredients) else ""
        )
        userDao.insertUser(encryptedUser)
    }

    suspend fun updateUser(user: User) {
        val encryptedUser = user.copy(
            allergies = if (user.allergies.isNotEmpty()) SecurityUtils.encrypt(user.allergies) else "",
            avoidedIngredients = if (user.avoidedIngredients.isNotEmpty()) SecurityUtils.encrypt(user.avoidedIngredients) else ""
        )
        userDao.updateUser(encryptedUser)
    }

    // User Scan History
    fun getHistory(username: String): Flow<List<FoodEntry>> = foodEntryDao.getHistoryForUser(username)
    fun getAllHistory(): Flow<List<FoodEntry>> = foodEntryDao.getAllEntries()
    suspend fun saveFoodEntry(entry: FoodEntry) = foodEntryDao.insertFoodEntry(entry)

    suspend fun scanFood(barcode: String, user: User?): FoodResponse {
        return try {
            val response = apiService.getProductByBarcode(barcode)
            if (response.status == 1 && response.product != null) {
                val product = response.product
                val allergens = product.allergens ?: ""
                val ingredients = product.ingredientsText ?: ""
                
                var isSafe = true
                val reasons = mutableListOf<String>()
                
                // Use actual preferences for safety check
                val userAllergies = user?.allergies ?: ""
                userAllergies.split(",").map { it.trim() }.forEach { allergy ->
                    if (allergy.isNotEmpty() && (allergens.contains(allergy, ignoreCase = true) || ingredients.contains(allergy, ignoreCase = true))) {
                        isSafe = false
                        reasons.add("Contains $allergy")
                    }
                }

                if (isSafe) {
                    FoodResponse("safe", "Safe for your profile", "Product: ${product.productName ?: "Unknown"}")
                } else {
                    FoodResponse("risky", reasons.joinToString(", "), "Avoid consumption")
                }
            } else {
                FoodResponse("unknown", "Product not found", "Try another barcode")
            }
        } catch (e: Exception) {
            FoodResponse("error", "API Error: ${e.message}", "Check your connection")
        }
    }

    // Farmer Blockchain Batches
    fun getFarmerBatches(username: String): Flow<List<FoodBatch>> = foodBatchDao.getBatchesForFarmer(username)
    
    suspend fun registerFoodBatch(batch: FoodBatch): FoodBatch {
        val blockchainResult = blockchainService.registerBatch(
            batch.batchId, batch.cropType, batch.origin, batch.certification
        )
        val verifiedBatch = batch.copy(
            status = blockchainResult.status,
            txHash = blockchainResult.txHash
        )
        foodBatchDao.insertBatch(verifiedBatch)
        return verifiedBatch
    }

    // Marketplace
    fun getAllMarketplaceItems(): Flow<List<MarketplaceItem>> = marketplaceItemDao.getAllAvailableItems()
    fun getFarmerProducts(username: String): Flow<List<MarketplaceItem>> = marketplaceItemDao.getItemsByFarmer(username)
    suspend fun addMarketplaceItem(item: MarketplaceItem) = marketplaceItemDao.insertItem(item)

    // Offers
    fun getFarmerOffers(username: String): Flow<List<Offer>> = offerDao.getOffersForFarmer(username)
    fun getBuyerOffers(username: String): Flow<List<Offer>> = offerDao.getOffersForBuyer(username)
    suspend fun createOffer(offer: Offer) = offerDao.insertOffer(offer)
    suspend fun updateOfferStatus(offer: Offer) = offerDao.updateOffer(offer)
}
