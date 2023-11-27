package com.indieme.kalorie.data.model

import com.google.gson.annotations.SerializedName

data class MealDTO(
    @SerializedName("mealId") var mealId: Int = 0,
    @SerializedName("meal") var meal: String? = null,
    @SerializedName("totalCalorie") var totalCalorie: Float = 0.0f,
    @SerializedName("addedOn") var addedOn: Long = 0L,
    @SerializedName("foodDTOList") var foodDTOList: ArrayList<TrackMealDTO> = arrayListOf()

)