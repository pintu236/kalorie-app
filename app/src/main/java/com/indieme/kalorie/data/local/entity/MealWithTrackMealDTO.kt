package com.indieme.kalorie.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

class MealWithTrackMealDTO {

    @Embedded
    var mealEntity: MealEntity? = null

    @Relation(parentColumn = "meal_id", entityColumn = "meal_id")
    var trackMealEntityList: MutableList<TrackMealEntity> = mutableListOf()


}