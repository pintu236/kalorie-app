package com.indieme.kalorie.data.network.repository

import com.indieme.kalorie.AppDatabase
import com.indieme.kalorie.data.local.RemoteCacheMapper
import com.indieme.kalorie.data.local.dao.TrackMealDao
import com.indieme.kalorie.data.local.entity.DateEntity
import com.indieme.kalorie.data.local.entity.MealEntity
import com.indieme.kalorie.data.manager.LocalPreferenceManager
import com.indieme.kalorie.data.model.DateResult
import com.indieme.kalorie.data.model.FoodDTO
import com.indieme.kalorie.data.model.StandardResponse
import com.indieme.kalorie.data.model.TrackMealDTO
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.data.model.UserProfileDTO
import com.indieme.kalorie.data.network.IAPIService
import com.indieme.kalorie.data.network.RestClient
import com.indieme.kalorie.utils.AppUtil
import com.indieme.kalorie.utils.TimingUtils
import java.util.Arrays


class AccountRepository(appDatabase: AppDatabase) {

    private val dateDao = appDatabase.dateDao()
    private val trackMealDao = appDatabase.trackMealDao()
    private val mealDao = appDatabase.mealDao()


    suspend fun registerUser(hashMap: HashMap<String, Any>): StandardResponse<UserDTO> {
        return RestClient.create(IAPIService::class.java).registerUser(hashMap)
    }

    suspend fun loginUser(hashMap: HashMap<String, Any>): StandardResponse<UserDTO> {
        return RestClient.create(IAPIService::class.java).loginUser(hashMap)
    }

    suspend fun getProfile(): StandardResponse<UserDTO> {
        return RestClient.create(IAPIService::class.java).getProfile()
    }

    suspend fun updateProfile(userProfileDTO: UserProfileDTO): StandardResponse<UserProfileDTO> {
        val result = RestClient.create(IAPIService::class.java).updateProfile(userProfileDTO)

        return result;
    }


    suspend fun deleteTrackFood(
        foodId: Int,
        servingSizeId: Int,
        mealId: Int,
        foodCalorie: Float,
        selectedDate: Long
    ): StandardResponse<String> {
        val result = RestClient.create(IAPIService::class.java)
            .deleteTrackFood(foodId, servingSizeId, mealId)
        //Remove From Local
        if (result.status == 200) {
            trackMealDao.delete(foodId, servingSizeId, mealId)
            //Check If Any Food exist in the meal
            val startOfADay = TimingUtils.getStartOfADayInMillis(selectedDate)
            val endOfADay = TimingUtils.getEndOfADayInMillis(selectedDate);

            val meal = mealDao.getMeal(startOfADay, endOfADay, mealId)

            if (meal != null) {
                //Update Calorie
                meal.totalCalorie = meal.totalCalorie?.minus(foodCalorie);
                mealDao.update(meal)
            } else {
                //Remote Track meal
                mealDao.delete(startOfADay, endOfADay, mealId)
            }
        }

        return result;
    }

    suspend fun addFoodForTracking(hashMap: HashMap<String, Any?>): StandardResponse<TrackMealDTO> {
        val result = RestClient.create(IAPIService::class.java).addTrackFood(hashMap)
        val resultTrackMeal =
            RestClient.create(IAPIService::class.java).getTrackMeals(result.response.addedOn);
        //Insert Meal
        mealDao.insert(RemoteCacheMapper.toMealEntityList(resultTrackMeal.response))
        //Insert food
        trackMealDao.insert(listOf(RemoteCacheMapper.toTrackMealEntity(result.response)))
        return result;
    }

    suspend fun getDates(): List<DateEntity> {
        if (isDatesCached()) {
            //Reset Old date
            dateDao.resetDate()
            // 1. Define a time window for today's date with a margin of error (e.g., 24 hours).
            val currentTimeInSeconds =
                System.currentTimeMillis() / 1000 // Convert milliseconds to seconds
            val windowStart = currentTimeInSeconds - (24 * 60 * 60) // Subtract 24 hours in seconds
            val windowEnd = currentTimeInSeconds + (24 * 60 * 60) // Add 24 hours in seconds

            dateDao.updateDatesInTimeWindow(windowStart, windowEnd)

            return dateDao.getDates()
        }

        val datesFromToday = TimingUtils.getDatesFromToday();
        dateDao.insert(RemoteCacheMapper.toDateEntity(datesFromToday.list))

        return dateDao.getDates()

    }


    private suspend fun isDatesCached(): Boolean {
        return dateDao.isExist()
    }
}