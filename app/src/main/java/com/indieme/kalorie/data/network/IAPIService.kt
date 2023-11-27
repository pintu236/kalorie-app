package com.indieme.kalorie.data.network

import com.indieme.kalorie.data.model.FoodDTO
import com.indieme.kalorie.data.model.MealDTO
import com.indieme.kalorie.data.model.StandardResponse
import com.indieme.kalorie.data.model.TrackMealDTO
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.data.model.UserProfileDTO
import com.indieme.kalorie.utils.U_ADD_TRACK_FOOD
import com.indieme.kalorie.utils.U_DELETE_TRACK_FOOD
import com.indieme.kalorie.utils.U_GET_PROFILE
import com.indieme.kalorie.utils.U_GET_TRACK_MEALS
import com.indieme.kalorie.utils.U_LOGIN
import com.indieme.kalorie.utils.U_REGISTER
import com.indieme.kalorie.utils.U_SEARCH_FOOD
import com.indieme.kalorie.utils.U_UPDATE_PROFILE
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IAPIService {

    @POST(U_REGISTER)
    suspend fun registerUser(@Body hashMap: HashMap<String, Any>): StandardResponse<UserDTO>

    @POST(U_LOGIN)
    suspend fun loginUser(@Body hashMap: HashMap<String, Any>): StandardResponse<UserDTO>

    @GET(U_GET_PROFILE)
    suspend fun getProfile(): StandardResponse<UserDTO>

    @POST(U_UPDATE_PROFILE)
    suspend fun updateProfile(@Body userProfileDTO: UserProfileDTO): StandardResponse<UserProfileDTO>

    @POST(U_ADD_TRACK_FOOD)
    suspend fun addTrackFood(@Body hashMap: HashMap<String, Any?>): StandardResponse<TrackMealDTO>

    @DELETE(U_DELETE_TRACK_FOOD)
    suspend fun deleteTrackFood(
        @Path("foodId") foodId: Int,
        @Path("servingSizeId") servingSizeId: Int,
        @Path("mealId") mealId: Int
    ): StandardResponse<String>

    @GET(U_GET_TRACK_MEALS)
    suspend fun getTrackMeals(@Query("added_on") addedOn: Long): StandardResponse<MutableList<MealDTO>>

    @GET(U_SEARCH_FOOD)
    suspend fun searchFood(@Query("query") query: String): StandardResponse<MutableList<FoodDTO>>

}