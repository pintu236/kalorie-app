package com.indieme.kalorie.ui.trackfood

import android.app.Application
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import com.indieme.kalorie.KalorieApplication
import com.indieme.kalorie.data.local.entity.DateEntity
import com.indieme.kalorie.data.local.entity.MealWithTrackMealDTO
import com.indieme.kalorie.data.local.entity.TrackMealEntity
import com.indieme.kalorie.data.model.DateDTO
import com.indieme.kalorie.data.model.MealDTO
import com.indieme.kalorie.data.model.TrackFoodDTO
import com.indieme.kalorie.data.network.repository.AccountRepository
import com.indieme.kalorie.data.network.repository.MealRepository
import com.indieme.kalorie.ui.base.BaseViewModel
import com.indieme.kalorie.utils.TimingUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TrackFoodCalorieViewModel(application: Application) : BaseViewModel(application) {
    private var selectedIndex: Int = 0
    private var mealRepository: MealRepository

    // Backing property to avoid state updates from other classes
    private val _trackFoodState: MutableStateFlow<TrackFoodUiState> =
        MutableStateFlow(TrackFoodUiState.Empty)

    // The UI collects from this StateFlow to get its state updates
    val trackFoodState: StateFlow<TrackFoodUiState> = _trackFoodState

    private val _dateChangeState: MutableStateFlow<DateChangeUiState> =
        MutableStateFlow(DateChangeUiState.Empty)
    val dateChangeState: StateFlow<DateChangeUiState> = _dateChangeState;


    private val _trackMealState: MutableStateFlow<TrackMealUiState> =
        MutableStateFlow(TrackMealUiState.Empty)
    val trackMealState: StateFlow<TrackMealUiState> = _trackMealState;

    private val accountRepository: AccountRepository =
        AccountRepository((application as KalorieApplication).database)

    init {
        val database = (application as KalorieApplication).database;

        mealRepository = MealRepository(database.mealDao(), database.trackMealDao())
        //
        initDates()

    }

    public fun getMeals(addedOn: Long) {
        viewModelScope.launch {
            _trackMealState.value = TrackMealUiState.Loading
            try {
                mealRepository.getTrackedMeals(addedOn)
                    .collect {
                        _trackMealState.value = TrackMealUiState.Success(it)
                    }

            } catch (e: Exception) {
                _trackMealState.value = TrackMealUiState.Error(parseError(e))
            }
        }
    }


    private fun initDates() {
        viewModelScope.launch {
            try {
                val result = accountRepository.getDates();
                selectedIndex = 0;
                var i = 0;
                result.forEach {
                    if (it.selected) {
                        selectedIndex = i;
                    }
                    i++;
                }
                _trackFoodState.value = TrackFoodUiState.Success(
                    TrackFoodDTO(
                        selectedIndex,
                        result.toMutableList()
                    )
                )
            } catch (e: Exception) {

            }

        }
    }

    fun onDateChange(dateDTO: DateEntity) {
        _dateChangeState.value = DateChangeUiState.Success(dateDTO)

    }

    fun delete(foodItem: TrackMealEntity, selectedDate: Long) {
        viewModelScope.launch {
            try {
                accountRepository.deleteTrackFood(
                    foodId = foodItem.foodId,
                    servingSizeId = foodItem.servingSizeId,
                    mealId = foodItem.mealId,
                    foodCalorie = foodItem.calorie ?: 0.0f,
                    selectedDate = selectedDate
                )

            } catch (e: Exception) {

            }
        }
    }


}


// Represents different states for the TrackCalorie screen
sealed class TrackFoodUiState {

    object Empty : TrackFoodUiState()
    object Loading : TrackFoodUiState()
    data class Success(val trackFoodDTO: TrackFoodDTO) : TrackFoodUiState()
    data class Error(val exception: String?) : TrackFoodUiState()
}

sealed class TrackMealUiState {

    object Empty : TrackMealUiState()
    object Loading : TrackMealUiState()
    data class Success(val mealDTO: List<MealWithTrackMealDTO>) : TrackMealUiState()
    data class Error(val exception: String?) : TrackMealUiState()
}

sealed class DateChangeUiState {

    object Empty : DateChangeUiState()
    object Loading : DateChangeUiState()
    data class Success(val dateDTO: DateEntity) : DateChangeUiState()
    data class Error(val exception: String?) : DateChangeUiState()
}