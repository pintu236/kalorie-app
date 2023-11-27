package com.indieme.kalorie.data.model

import com.google.gson.annotations.SerializedName

data class FoodNutrientsDTO(
    @SerializedName("nutrientId") var nutrientId: Int? = null,
    @SerializedName("servingSizeId") var servingSizeId: Int? = null,
    @SerializedName("value") var value: Float? = null
)
