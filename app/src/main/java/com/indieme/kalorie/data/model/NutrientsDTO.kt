package com.indieme.kalorie.data.model

import com.google.gson.annotations.SerializedName

data class NutrientsDTO(
    @SerializedName("servingSizeId") var servingSizeId: Int? = null,
    @SerializedName("servingSizeName") var servingSizeName: String? = null,
    @SerializedName("foodNutrients") var foodNutrients: ArrayList<FoodNutrientsDTO> = arrayListOf()
)
