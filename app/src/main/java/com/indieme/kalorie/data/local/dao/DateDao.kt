package com.indieme.kalorie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.indieme.kalorie.data.local.entity.DateEntity
import com.indieme.kalorie.data.local.entity.MealEntity
import com.indieme.kalorie.data.local.entity.MealWithTrackMealDTO
import com.indieme.kalorie.data.local.entity.TrackMealEntity

@Dao
interface DateDao {

    @Upsert
    suspend fun insert(dateEntityList: List<DateEntity>)


    @Query("SELECT * FROM calendar_date")
    suspend fun getDates(): List<DateEntity>

    @Query("SELECT EXISTS(SELECT * FROM calendar_date)")
    suspend fun isExist(): Boolean

    @Query("UPDATE calendar_date set selected= 0 where selected = 1")
    suspend fun resetDate()

    @Query("UPDATE calendar_date SET selected = 1 WHERE time_in_seconds >= :windowStart AND time_in_seconds <= :windowEnd")
    suspend fun updateDatesInTimeWindow(windowStart: Long, windowEnd: Long)


}