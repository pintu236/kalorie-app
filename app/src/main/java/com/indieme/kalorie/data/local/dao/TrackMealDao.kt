package com.indieme.kalorie.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.indieme.kalorie.data.local.entity.MealEntity
import com.indieme.kalorie.data.local.entity.MealWithTrackMealDTO
import com.indieme.kalorie.data.local.entity.TrackMealEntity

@Dao
interface TrackMealDao {

    @Upsert
    suspend fun insert(trackMealEntityList: List<TrackMealEntity>)

    @Query("DELETE FROM track_meal WHERE food_id =:foodId AND serving_size_id =:servingSizeId AND meal_id =:mealId")
    suspend fun delete(foodId: Int, servingSizeId: Int, mealId: Int)




}