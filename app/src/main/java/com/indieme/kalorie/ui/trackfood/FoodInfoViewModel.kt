package com.indieme.kalorie.ui.trackfood

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.indieme.kalorie.KalorieApplication
import com.indieme.kalorie.data.manager.LocalAppMemCache
import com.indieme.kalorie.data.model.FoodDTO
import com.indieme.kalorie.data.model.TrackMealDTO
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.data.network.repository.AccountRepository
import com.indieme.kalorie.ui.base.BaseViewModel
import com.indieme.kalorie.ui.login.LoginUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodInfoViewModel(application: Application) : BaseViewModel(application) {
    // Backing property to avoid state updates from other classes
    private val _foodInfoState: MutableStateFlow<FoodInfoUiState> =
        MutableStateFlow(FoodInfoUiState.Empty)

    // The UI collects from this StateFlow to get its state updates
    val foodInfoState: StateFlow<FoodInfoUiState> = _foodInfoState

    private val accountRepository: AccountRepository =
        AccountRepository((application as KalorieApplication).database)

    fun addFoodTracking(hashMap: HashMap<String, Any?>) {
        _foodInfoState.value = FoodInfoUiState.Loading
        viewModelScope.launch {
            try {
                val result = accountRepository.addFoodForTracking(hashMap)
                _foodInfoState.value = FoodInfoUiState.Success(result.response, result.message)
                delay(200)
                _foodInfoState.value = FoodInfoUiState.Empty
            } catch (e: Exception) {
                _foodInfoState.value = FoodInfoUiState.Error(parseError(e))
            }
        }
    }
}


// Represents different states for the Login screen
sealed class FoodInfoUiState {

    object Empty : FoodInfoUiState()
    object Loading : FoodInfoUiState()
    data class Success(val foodDTO: TrackMealDTO, val message: String) : FoodInfoUiState()
    data class Error(val exception: String?) : FoodInfoUiState()
}