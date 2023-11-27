package com.indieme.kalorie.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.indieme.kalorie.MainUiState
import com.indieme.kalorie.MainViewModel
import com.indieme.kalorie.Screen
import com.indieme.kalorie.ui.trackfood.TrackFoodUiState

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current;




    LaunchedEffect(Unit) {
        viewModel.trackFoodState.collect { trackUiState ->
            when (trackUiState) {
                TrackFoodUiState.Empty -> {

                }

                is TrackFoodUiState.Success -> {
                    viewModel.isLoggedIn(context)
                }

                is TrackFoodUiState.Error -> {
                    viewModel.isLoggedIn(context)
                }

                TrackFoodUiState.Loading -> {

                }

            }

        }
    }

    LaunchedEffect(Unit) {
        viewModel.loggedInState.collect { uiState ->
            if (uiState is MainUiState.Success) {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(0)
                }

            } else if (uiState is MainUiState.Error) {
                navController.navigate(Screen.Welcome.route) {
                    popUpTo(0)
                }
            }
        }
    }


}