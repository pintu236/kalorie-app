package com.indieme.kalorie.data.model

import com.google.gson.annotations.SerializedName

data class FoodDTO(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("categoryId") var categoryId: Int? = null,
    @SerializedName("servingSizeId") var servingSizeId: Int? = null,
    @SerializedName("nutrients") var nutrients: ArrayList<NutrientsDTO> = arrayListOf(),
    @SerializedName("categoryName") var categoryName: String? = null
)