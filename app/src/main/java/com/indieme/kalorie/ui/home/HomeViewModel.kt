package com.indieme.kalorie.ui.home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.indieme.kalorie.KalorieApplication
import com.indieme.kalorie.data.manager.LocalAppMemCache
import com.indieme.kalorie.data.manager.LocalPreferenceManager
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.data.network.repository.AccountRepository
import com.indieme.kalorie.data.network.repository.MealRepository
import com.indieme.kalorie.ui.base.BaseViewModel
import com.indieme.kalorie.ui.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(private val application: Application) : BaseViewModel(application) {

    // Backing property to avoid state updates from other classes
    private val _userState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Empty)

    // The UI collects from this StateFlow to get its state updates
    val userState: StateFlow<HomeUiState> = _userState
    val accountRepository = AccountRepository((application as KalorieApplication).database)
    val mealRepository = MealRepository(
        (application as KalorieApplication).database.mealDao(),
        application.database.trackMealDao()
    )
    val bmrFlow = LocalPreferenceManager.readBMR(application)
    val TDEEFlow = LocalPreferenceManager.readTDEE(application)
    val dailyCalorieFlow = LocalPreferenceManager.readDailyCalorie(application)
    val bmiFlow = LocalPreferenceManager.readBMI(application)
    val consumedCalorieFlow = LocalPreferenceManager.readDailyConsumedCalorie(application)
    val consumedMealsFlow = mealRepository.getMealsFlow(System.currentTimeMillis() / 1000);


    init {

        viewModelScope.launch {
            try {
                LocalPreferenceManager.readUserData(application.applicationContext)?.let {
                    LocalAppMemCache.userBMR = it.bmr
                    _userState.value =
                        HomeUiState.Success(it)
                }

            } catch (e: Exception) {

            }
        }


    }


}


// Represents different states for the Home screen
sealed class HomeUiState {

    object Empty : HomeUiState()
    object Loading : HomeUiState()
    data class Success(val userDTO: UserDTO) : HomeUiState()
    data class Error(val exception: String?) : HomeUiState()
}