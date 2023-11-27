package com.indieme.kalorie

import androidx.compose.material.icons.Icons

sealed class Screen(
    val route: String,
    val defaultResId: Int? = null,
    val selectedResId: Int? = null
) {
    object Welcome : Screen(route = "welcome")
    object Login : Screen(route = "login")
    object Register : Screen(route = "register")
    object Dashboard : Screen(route = "dashboard")
    object Splash : Screen(route = "splash")
    object GenderOnBoarding : Screen(route = "gender_onboarding")
    object AgeOnBoarding : Screen(route = "age_onboarding")
    object WeightHeightOnBoarding : Screen(route = "weight_height_onboarding")
    object TrackFoodCalorie : Screen(route = "track_food_calorie")
    object SearchAndAddFood : Screen(route = "search_and_add_food")
    object EditProfile : Screen(route = "edit_profile")
    object FoodInfo : Screen(route = "food_info")
    object Home : Screen(route = "home", R.drawable.home_light, R.drawable.home_light)
    object Search : Screen(route = "search", R.drawable.search, R.drawable.search)
    object Scan : Screen(route = "scan", R.drawable.round_add_24, R.drawable.round_add_24)
    object Favorite : Screen(route = "favorite", R.drawable.heart, R.drawable.heart)
    object Profile : Screen(route = "profile", R.drawable.profile, R.drawable.profile)

}