package com.indieme.kalorie.data.network.repository

import com.indieme.kalorie.data.local.entity.MealEntity
import com.indieme.kalorie.data.local.entity.MealWithTrackMealDTO
import com.indieme.kalorie.data.local.entity.TrackMealEntity
import com.indieme.kalorie.data.model.MealDTO
import com.indieme.kalorie.data.model.StandardResponse
import com.indieme.kalorie.data.network.IAPIService
import com.indieme.kalorie.data.network.RestClient
import kotlinx.coroutines.flow.Flow

interface AbstractMealRepository {


    suspend fun syncTrackedMeals(addedOn: Long): StandardResponse<MutableList<MealDTO>>

    suspend fun getTrackedMeals(addedOn: Long): Flow<MutableList<MealWithTrackMealDTO>>

    suspend fun getSavedMeals(addedOn: Long): Flow<MutableList<MealWithTrackMealDTO>>

    fun getMealsFlow(addedOn: Long): Flow<MutableList<TrackMealEntity>>

    suspend fun saveTrackedMeals(mealDTOList: MutableList<MealDTO>)

    suspend fun isMealCached(addedOn: Long): Boolean

}