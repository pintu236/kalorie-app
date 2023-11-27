package com.indieme.kalorie.ui.login

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.indieme.kalorie.KalorieApplication
import com.indieme.kalorie.data.manager.LocalAppMemCache
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.data.network.repository.AccountRepository
import com.indieme.kalorie.ui.base.BaseViewModel
import com.indieme.kalorie.ui.register.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseViewModel(application) {
    // Backing property to avoid state updates from other classes
    private val _userState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState.Empty)

    // The UI collects from this StateFlow to get its state updates
    val userState: StateFlow<LoginUiState> = _userState
    private val accountRepository: AccountRepository =
        AccountRepository((application as KalorieApplication).database)

    fun loginUser(hashMap: HashMap<String, Any>) {
        _userState.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                val result = accountRepository.loginUser(hashMap)
                _userState.value = LoginUiState.Success(result.response)
                LocalAppMemCache.token = result.response.token

            } catch (e: Exception) {
                _userState.value = LoginUiState.Error(parseError(e))
            }


        }
    }
}


// Represents different states for the Login screen
sealed class LoginUiState {

    object Empty : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val userDTO: UserDTO) : LoginUiState()
    data class Error(val exception: String?) : LoginUiState()
}