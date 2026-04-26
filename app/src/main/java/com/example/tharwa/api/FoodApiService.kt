package com.example.tharwa.api

import retrofit2.http.GET
import retrofit2.http.Path

interface FoodApiService {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(@Path("barcode") barcode: String): OpenFoodFactsResponse
}
