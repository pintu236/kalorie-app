package com.indieme.kalorie.data.model

import com.google.gson.annotations.SerializedName
import com.indieme.kalorie.data.local.entity.MealWithTrackMealDTO

class StandardResponse<T>(
    @SerializedName("response")
    var response: T,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Int
) {

    companion object {
        fun <T> success(response: T): StandardResponse<T> {
            return StandardResponse(response, "", 0)
        }
    }
}