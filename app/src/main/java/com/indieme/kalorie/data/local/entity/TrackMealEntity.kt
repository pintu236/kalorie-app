package com.indieme.kalorie.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "track_meal",
    primaryKeys = ["food_id", "meal_id", "serving_size_id"],
    foreignKeys = [ForeignKey(
        entity = MealEntity::class,
        childColumns = ["meal_id", "added_on"],
        parentColumns = ["meal_id", "added_on"]
    )]
)
data class TrackMealEntity(

    @ColumnInfo(name = "food_id")
    var foodId: Int,
    @ColumnInfo(name = "meal_id")
    var mealId: Int,
    @ColumnInfo(name = "serving_size_id")
    var servingSizeId: Int,
    @ColumnInfo(name = "added_on")
    var addedOn: Date,
    @ColumnInfo(name = "no_of_serving")
    var noOfServing: Int? = null,
    @ColumnInfo(name = "food_name")
    var foodName: String? = null,
    @ColumnInfo(name = "serving_size")
    var servingSize: String? = null,
    @ColumnInfo(name = "calorie")
    var calorie: Float? = null,
)