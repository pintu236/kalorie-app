package com.indieme.kalorie

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.indieme.kalorie.data.local.DateConverter
import com.indieme.kalorie.data.local.dao.DateDao
import com.indieme.kalorie.data.local.dao.MealDao
import com.indieme.kalorie.data.local.dao.TrackMealDao
import com.indieme.kalorie.data.local.entity.DateEntity
import com.indieme.kalorie.data.local.entity.MealEntity
import com.indieme.kalorie.data.local.entity.MealWithTrackMealDTO
import com.indieme.kalorie.data.local.entity.TrackMealEntity

@Database(
    entities = [MealEntity::class, TrackMealEntity::class, DateEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dateDao(): DateDao

    abstract fun mealDao(): MealDao

    abstract fun trackMealDao(): TrackMealDao

}