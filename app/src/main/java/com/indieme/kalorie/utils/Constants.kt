package com.indieme.kalorie.utils

//Keys
const val K_BEARER = "Bearer"
const val K_TOKEN = "token"
const val K_ID = "id"
const val K_CURRENT_WEIGHT = "currentWeight"
const val K_HEIGHT = "height"
const val K_USERNAME = "username"
const val K_GENDER = "gender"
const val K_BMR = "bmr"
const val K_TDEE = "tdee"
const val K_BMI = "bmi"
const val K_REMAINING_CALORIE = "remaining_calorie"
const val K_CONSUMED_CALORIE = "consumed_calorie"
const val K_DALIY_CALORIE = "daily_calorie_intake"
const val K_AGE = "age"
const val K_EMAIL = "email"
const val K_PASSWORD = "password"
const val K_ADDED_ON = "addedOn"
const val K_SERVING_SIZE_ID = "servingSizeId"
const val K_MEAL_ID = "mealId"
const val K_NO_OF_SERVING = "noOfServing"
const val K_FOOD_ID = "foodId"


//URLs
const val U_REGISTER = "register"
const val U_LOGIN = "login"
const val U_SEARCH_FOOD = "food/search-food"
const val U_ADD_TRACK_FOOD = "user/add-track-food"
const val U_DELETE_TRACK_FOOD = "food/{foodId}/{servingSizeId}/{mealId}"
const val U_GET_TRACK_MEALS = "user/get-track-meals"
const val U_GET_PROFILE = "get-profile"
const val U_UPDATE_PROFILE = "save-user-profile"


//Nutrients types
const val TYPE_PROTIEN = 1;
const val TYPE_CARBS = 2;
const val TYPE_FAT = 3;
const val TYPE_CALORIE = 4;
const val TYPE_FIRBE = 5;

//Meal types
const val MEAL_SNACKS = 4
const val MEAL_BREAKFAST = 1
const val MEAL_LUNCH = 2
const val MEAL_DINNER = 3

//Temp Strings
val morningGreeting = "Good morning!"
val afternoonGreeting = "Good afternoon!"
val eveningGreeting = "Good evening!"
val nightGreeting = "Good night!"