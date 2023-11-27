package com.indieme.kalorie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.indieme.kalorie.ui.DashboardScreen
import com.indieme.kalorie.ui.SplashScreen
import com.indieme.kalorie.ui.composables.WelcomeScreen
import com.indieme.kalorie.ui.login.LoginScreen
import com.indieme.kalorie.ui.onboarding.AgeOnBoardingScreen
import com.indieme.kalorie.ui.onboarding.GenderOnBoardingScreen
import com.indieme.kalorie.ui.onboarding.WeightHeightOnBoardingScreen
import com.indieme.kalorie.ui.profile.EditProfileScreen
import com.indieme.kalorie.ui.register.RegisterScreen
import com.indieme.kalorie.ui.theme.KalorieTheme
import com.indieme.kalorie.ui.trackfood.SearchAndAddFoodScreen
import com.indieme.kalorie.utils.MEAL_BREAKFAST

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KalorieTheme {
                KalorieApp()
            }
        }

    }

}


@Composable
fun KalorieApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {


    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(parentNavController = navController)
        }
        composable(route = Screen.EditProfile.route) {
            EditProfileScreen(parentNavController = navController)
        }

        composable(route = Screen.GenderOnBoarding.route) {
            GenderOnBoardingScreen(parentNavController = navController)
        }
        composable(route = Screen.AgeOnBoarding.route) {
            AgeOnBoardingScreen(parentNavController = navController)
        }
        composable(route = Screen.WeightHeightOnBoarding.route) {
            WeightHeightOnBoardingScreen(parentNavController = navController)
        }
        composable(route = "${Screen.SearchAndAddFood.route}?value={value}&date={date}",
            arguments = listOf(
                navArgument("value") {
                    type = NavType.IntType
                    defaultValue = MEAL_BREAKFAST
                },
                navArgument("date") {
                    type = NavType.LongType
                    defaultValue = 0
                }
            )) { backStackEntry ->
            SearchAndAddFoodScreen(
                navController = navController,
                backStackEntry.arguments?.getInt("value") ?: MEAL_BREAKFAST,
                backStackEntry.arguments?.getLong("date") ?: 0
            )
        }


    }

}

