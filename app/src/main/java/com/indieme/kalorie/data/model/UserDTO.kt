package com.indieme.kalorie.data.model

data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val token: String?,
    val bmr: String?,
    val tdee: String?,
    val dailyCalorieIntake: String?,
    var userProfile: UserProfileDTO?,
    val goalTypes: List<String> = listOf(),
    val activityLevels: List<String> = listOf(),
    val weightUnits: List<String> = listOf(),
    val heightUnits: List<String> = listOf()
)
