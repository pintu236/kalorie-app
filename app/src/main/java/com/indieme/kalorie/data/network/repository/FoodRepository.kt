package com.indieme.kalorie.data.network.repository

import com.indieme.kalorie.data.model.FoodDTO
import com.indieme.kalorie.data.model.StandardResponse
import com.indieme.kalorie.data.network.IAPIService
import com.indieme.kalorie.data.network.RestClient

object FoodRepository {

    suspend fun searchFood(query: String): StandardResponse<MutableList<FoodDTO>> {
        return RestClient.create(IAPIService::class.java).searchFood(query)
    }
}