package com.indieme.kalorie.data.network.repository

import com.indieme.kalorie.data.local.RemoteCacheMapper
import com.indieme.kalorie.data.local.dao.MealDao
import com.indieme.kalorie.data.local.dao.TrackMealDao
import com.indieme.kalorie.data.local.entity.MealWithTrackMealDTO
import com.indieme.kalorie.data.local.entity.TrackMealEntity
import com.indieme.kalorie.data.model.MealDTO
import com.indieme.kalorie.data.model.StandardResponse
import com.indieme.kalorie.data.network.IAPIService
import com.indieme.kalorie.data.network.RestClient
import com.indieme.kalorie.utils.TimingUtils
import kotlinx.coroutines.flow.Flow

class MealRepository(
    private val mealDao: MealDao,
    private val trackMealDao: TrackMealDao
) : AbstractMealRepository {
    override suspend fun syncTrackedMeals(addedOn: Long): StandardResponse<MutableList<MealDTO>> {
        val response = RestClient.create(IAPIService::class.java).getTrackMeals(addedOn)
        saveTrackedMeals(response.response)
        return response;
    }

    override suspend fun getTrackedMeals(addedOn: Long): Flow<MutableList<MealWithTrackMealDTO>> {
        if (isMealCached(addedOn)) {
            return getSavedMeals(addedOn)
        }

        val response = RestClient.create(IAPIService::class.java).getTrackMeals(addedOn)
        saveTrackedMeals(response.response)

        return getSavedMeals(addedOn)
    }


    override suspend fun getSavedMeals(addedOn: Long): Flow<MutableList<MealWithTrackMealDTO>> {
        val startOfADay = TimingUtils.getStartOfADayInMillis(addedOn)
        val endOfADay = TimingUtils.getEndOfADayInMillis(addedOn);


        return mealDao.getMeals(
            startOfADay,
            endOfADay
        )
    }

    override fun getMealsFlow(addedOn: Long): Flow<MutableList<TrackMealEntity>> {
        val startOfADay = TimingUtils.getStartOfADayInMillis(addedOn)
        val endOfADay = TimingUtils.getEndOfADayInMillis(addedOn);
        return mealDao.getMealsFlow(startOfADay, endOfADay)
    }

    override suspend fun saveTrackedMeals(mealDTOList: MutableList<MealDTO>) {
        mealDao.insert(RemoteCacheMapper.toMealEntityList(mealDTOList))
        trackMealDao.insert(RemoteCacheMapper.toTrackMealEntityList(mealDTOList))
    }

    override suspend fun isMealCached(addedOn: Long): Boolean {
        val startOfADay = TimingUtils.getStartOfADayInMillis(addedOn)
        val endOfADay = TimingUtils.getEndOfADayInMillis(addedOn);

        return mealDao.isExist(startOfADay, endOfADay)
    }


}