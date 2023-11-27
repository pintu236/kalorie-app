package com.indieme.kalorie

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.indieme.kalorie.data.manager.LocalAppMemCache
import com.indieme.kalorie.data.manager.LocalPreferenceManager
import com.indieme.kalorie.data.model.TrackFoodDTO
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.data.network.repository.AccountRepository
import com.indieme.kalorie.ui.base.BaseViewModel
import com.indieme.kalorie.ui.register.RegisterUiState
import com.indieme.kalorie.ui.trackfood.TrackFoodUiState
import com.indieme.kalorie.utils.TimingUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {
    // Backing property to avoid state updates from other classes
    private val _loggedInState: MutableStateFlow<MainUiState> =
        MutableStateFlow(MainUiState.Empty)

    // The UI collects from this StateFlow to get its state updates
    val loggedInState: StateFlow<MainUiState> = _loggedInState

    // Backing property to avoid state updates from other classes
    private val _trackFoodState: MutableStateFlow<TrackFoodUiState> =
        MutableStateFlow(TrackFoodUiState.Empty)

    // The UI collects from this StateFlow to get its state updates
    val trackFoodState: StateFlow<TrackFoodUiState> = _trackFoodState
    private val accountRepository: AccountRepository =
        AccountRepository((application as KalorieApplication).database)

    init {
        initDates()
    }


    fun isLoggedIn(context: Context) {
        viewModelScope.launch {
            try {
                val token = LocalPreferenceManager.readUserData(context)?.token
                if (token?.isEmpty() == true) {
                    _loggedInState.value = MainUiState.Error("Not Logged In")
                    LocalAppMemCache.token = null
                } else {
                    LocalAppMemCache.token = token
                    _loggedInState.value = MainUiState.Success(true)
                }

            } catch (e: Exception) {
                _loggedInState.value = MainUiState.Error(parseError(e))
            }


        }
    }


    private fun initDates() {
        viewModelScope.launch {
            try {
                val result = accountRepository.getDates()

                _trackFoodState.value = TrackFoodUiState.Success(
                    TrackFoodDTO(
                        0,
                        result.toMutableList()
                    )
                )
            } catch (e: Exception) {
                _trackFoodState.value = TrackFoodUiState.Error(e.message)
            }

        }
    }

}

// Represents different states for the Register screen
sealed class MainUiState {

    object Empty : MainUiState()
    object Loading : MainUiState()
    data class Success(val userDTO: Boolean) : MainUiState()
    data class Error(val exception: String?) : MainUiState()
}
