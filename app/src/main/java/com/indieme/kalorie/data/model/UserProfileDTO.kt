package com.indieme.kalorie.data.model

data class UserProfileDTO(
    val currentWeight: String?,
    val gender: String?,
    val age: String?,
    val currentHeight: String?,
    var goalType: String?,
    val targetWeight: String?,
    var targetDate: String?,
    var activityLevel: String?,
    var weightUnit: String?,
    var heightUnit: String?,
){

}