package com.indieme.kalorie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.indieme.kalorie.data.local.entity.MealEntity
import com.indieme.kalorie.data.local.entity.MealWithTrackMealDTO
import com.indieme.kalorie.data.local.entity.TrackMealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Upsert
    suspend fun insert(mealEntityList: List<MealEntity>)

    @Query("DELETE FROM meal WHERE added_on BETWEEN :startDate AND :endDate AND meal_id =:mealId")
    suspend fun delete(startDate: Long, endDate: Long, mealId: Int)


    @Transaction
    @Query("SELECT * FROM meal WHERE added_on BETWEEN :startDate AND :endDate")
    fun getMeals(startDate: Long, endDate: Long): Flow<MutableList<MealWithTrackMealDTO>>

    @Transaction
    @Query("SELECT * FROM track_meal WHERE added_on BETWEEN :startDate AND :endDate")
    fun getMealsFlow(startDate: Long, endDate: Long): Flow<MutableList<TrackMealEntity>>

    @Query("SELECT EXISTS(SELECT * FROM meal WHERE added_on BETWEEN :startDate AND :endDate)")
    suspend fun isExist(startDate: Long, endDate: Long): Boolean

    @Query("UPDATE meal SET total_calorie = total_calorie - :foodCalorie WHERE meal_id =:mealId")
    suspend fun minusCalorie(mealId: Int, foodCalorie: Float)

    @Query("SELECT * FROM meal WHERE added_on BETWEEN :startOfADay AND :endOfADay AND  meal_id =:mealId")
    suspend fun getMeal(startOfADay: Long, endOfADay: Long, mealId: Int): MealEntity?


    @Update
    suspend fun update(mealEntity: MealEntity)


}