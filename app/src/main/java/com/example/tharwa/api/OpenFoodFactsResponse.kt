package com.example.tharwa.api

import com.google.gson.annotations.SerializedName

data class OpenFoodFactsResponse(
    val code: String?,
    val product: Product?,
    val status: Int?,
    @SerializedName("status_verbose") val statusVerbose: String?
)

data class Product(
    @SerializedName("product_name") val productName: String?,
    @SerializedName("ingredients_text") val ingredientsText: String?,
    val allergens: String?,
    val brands: String?,
    @SerializedName("image_url") val imageUrl: String?
)
