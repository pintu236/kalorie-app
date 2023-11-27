package com.indieme.kalorie.ui.trackfood

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.indieme.kalorie.data.model.FoodDTO
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.data.network.repository.FoodRepository
import com.indieme.kalorie.ui.base.BaseViewModel
import com.indieme.kalorie.ui.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchAddFoodViewModel(application: Application) : BaseViewModel(application) {

    // Backing property to avoid state updates from other classes
    private val _searchFoodState: MutableStateFlow<SearchAddFoodViewModelUiState> =
        MutableStateFlow(SearchAddFoodViewModelUiState.Empty)

    // The UI collects from this StateFlow to get its state updates
    val searchFoodState: StateFlow<SearchAddFoodViewModelUiState> = _searchFoodState


    fun searchFood(query: String) {
        _searchFoodState.value = SearchAddFoodViewModelUiState.Loading
        viewModelScope.launch {
            try {
                val result = FoodRepository.searchFood(query)
                _searchFoodState.value = SearchAddFoodViewModelUiState.Success(result.response)
            } catch (e: Exception) {
                _searchFoodState.value = SearchAddFoodViewModelUiState.Error(parseError(e))

            }
        }
    }

}


// Represents different states for the Login screen
sealed class SearchAddFoodViewModelUiState {

    object Empty : SearchAddFoodViewModelUiState()
    object Loading : SearchAddFoodViewModelUiState()
    data class Success(val foodList: List<FoodDTO>) : SearchAddFoodViewModelUiState()
    data class Error(val exception: String?) : SearchAddFoodViewModelUiState()
}
