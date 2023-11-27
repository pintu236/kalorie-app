package com.indieme.kalorie.ui.register

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.indieme.kalorie.KalorieApplication
import com.indieme.kalorie.data.model.NetworkResponse
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.data.network.repository.AccountRepository
import com.indieme.kalorie.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : BaseViewModel(application) {

    // Backing property to avoid state updates from other classes
    private val _userState: MutableStateFlow<RegisterUiState> =
        MutableStateFlow(RegisterUiState.Empty)

    // The UI collects from this StateFlow to get its state updates
    val userState: StateFlow<RegisterUiState> = _userState
    private val accountRepository: AccountRepository =
        AccountRepository((application as KalorieApplication).database)

    fun registerUser(hashMap: HashMap<String, Any>) {

        _userState.value = RegisterUiState.Loading
        viewModelScope.launch {
            try {
                val result = accountRepository.registerUser(hashMap)
                _userState.value = RegisterUiState.Success(result.response)

            } catch (e: Exception) {
                _userState.value = RegisterUiState.Error(parseError(e))
            }


        }
    }

}

// Represents different states for the Register screen
sealed class RegisterUiState {

    object Empty : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val userDTO: UserDTO) : RegisterUiState()
    data class Error(val exception: String?) : RegisterUiState()
}