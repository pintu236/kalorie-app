package com.indieme.kalorie.data.model

import com.google.gson.annotations.SerializedName

data class TrackMealDTO(
    @SerializedName("addedOn") var addedOn: Long,
    @SerializedName("foodId") var foodId: Int,
    @SerializedName("mealId") var mealId: Int,
    @SerializedName("servingSizeId") var servingSizeId: Int,
    @SerializedName("noOfServing") var noOfServing: Int? = null,
    @SerializedName("foodName") var foodName: String? = null,
    @SerializedName("servingSize") var servingSize: String? = null,
    @SerializedName("calorie") var calorie: Float = 0.0f
)
