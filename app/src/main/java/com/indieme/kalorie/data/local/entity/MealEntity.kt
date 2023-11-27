package com.indieme.kalorie.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date


@Entity(
    tableName = "meal",
    primaryKeys = ["meal_id", "added_on"]
)
data class MealEntity(
    @ColumnInfo(name = "meal_id")
    var mealId: Int = 0,
    @ColumnInfo(name = "meal")
    var meal: String? = null,
    @ColumnInfo(name = "total_calorie")
    var totalCalorie: Float? = null,
    @ColumnInfo(name = "added_on")
    var addedOn: Date
)