package com.example.tharwa.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tharwa.api.LocalAIAnalyzer
import com.example.tharwa.api.FoodResponse
import com.example.tharwa.data.*
import com.example.tharwa.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppRepository, context: Context) : ViewModel() {

    private val localAI = LocalAIAnalyzer(context)

    private val _currentUser = mutableStateOf<User?>(null)
    val currentUser: State<User?> = _currentUser

    private val _history = MutableStateFlow<List<FoodEntry>>(emptyList())
    val history: StateFlow<List<FoodEntry>> = _history

    private val _farmerBatches = MutableStateFlow<List<FoodBatch>>(emptyList())
    val farmerBatches: StateFlow<List<FoodBatch>> = _farmerBatches

    private val _marketplaceItems = MutableStateFlow<List<MarketplaceItem>>(emptyList())
    val marketplaceItems: StateFlow<List<MarketplaceItem>> = _marketplaceItems

    private val _incomingOffers = MutableStateFlow<List<Offer>>(emptyList())
    val incomingOffers: StateFlow<List<Offer>> = _incomingOffers

    fun signIn(username: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUser(username)
            if (user != null) {
                _currentUser.value = user
                observeUserData(user)
                onSuccess()
            } else {
                onError("User not found")
            }
        }
    }

    fun signUp(user: User, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.insertUser(user)
            _currentUser.value = user
            observeUserData(user)
            onSuccess()
        }
    }

    fun signOut(onSignOut: () -> Unit) {
        _currentUser.value = null
        _history.value = emptyList()
        _farmerBatches.value = emptyList()
        _incomingOffers.value = emptyList()
        onSignOut()
    }

    private fun observeUserData(user: User) {
        viewModelScope.launch {
            if (user.role == "Farmer") {
                launch { repository.getAllHistory().collectLatest { _history.value = it } }
                launch { repository.getFarmerBatches(user.username).collectLatest { _farmerBatches.value = it } }
                launch { repository.getFarmerOffers(user.username).collectLatest { _incomingOffers.value = it } }
            } else {
                launch { repository.getHistory(user.username).collectLatest { _history.value = it } }
            }
            launch { repository.getAllMarketplaceItems().collectLatest { _marketplaceItems.value = it } }
        }
    }

    fun updatePreferences(allergies: String, diet: String, avoidedIngredients: String = "") {
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(
            allergies = allergies, 
            dietaryPreferences = diet,
            avoidedIngredients = avoidedIngredients
        )
        viewModelScope.launch {
            repository.updateUser(updatedUser)
            _currentUser.value = updatedUser
        }
    }

    fun scanFood(barcode: String, onResult: (FoodEntry) -> Unit) {
        viewModelScope.launch {
            // First try Local AI for privacy
            val response = localAI.analyzeFoodLocally(barcode)
            val entry = FoodEntry(
                username = _currentUser.value?.username ?: "unknown",
                foodName = "Product: $barcode",
                status = response.status,
                reason = response.reason,
                recommendation = response.recommendation
            )
            repository.saveFoodEntry(entry)
            onResult(entry)
        }
    }

    fun addManualEntry(foodName: String, status: String, reason: String, recommendation: String) {
        viewModelScope.launch {
            val entry = FoodEntry(
                username = _currentUser.value?.username ?: "Farmer",
                foodName = foodName,
                status = status,
                reason = reason,
                recommendation = recommendation
            )
            repository.saveFoodEntry(entry)
        }
    }

    fun registerBatch(batch: FoodBatch, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.registerFoodBatch(batch)
            onComplete()
        }
    }

    fun addProduct(name: String, price: String, emoji: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.addMarketplaceItem(
                MarketplaceItem(
                    name = name,
                    price = price,
                    farm = if (user.farmerName.isNotEmpty()) user.farmerName else user.username,
                    imageEmoji = emoji,
                    farmerUsername = user.username
                )
            )
        }
    }

    fun buyProduct(item: MarketplaceItem, onComplete: () -> Unit) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.createOffer(
                Offer(
                    itemId = item.id,
                    itemName = item.name,
                    buyerUsername = user.username,
                    farmerUsername = item.farmerUsername,
                    price = item.price
                )
            )
            onComplete()
        }
    }

    fun acceptOffer(offer: Offer) {
        viewModelScope.launch {
            repository.updateOfferStatus(offer.copy(status = "Accepted"))
        }
    }
}
