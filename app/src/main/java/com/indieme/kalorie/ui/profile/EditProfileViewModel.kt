package com.indieme.kalorie.ui.profile

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.indieme.kalorie.KalorieApplication
import com.indieme.kalorie.data.manager.LocalAppMemCache
import com.indieme.kalorie.data.manager.LocalPreferenceManager
import com.indieme.kalorie.data.model.ActivityLevel
import com.indieme.kalorie.data.model.GoalType
import com.indieme.kalorie.data.model.HeightUnit
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.data.model.UserProfileDTO
import com.indieme.kalorie.data.model.WeightUnit
import com.indieme.kalorie.data.network.repository.AccountRepository
import com.indieme.kalorie.ui.base.BaseViewModel
import com.indieme.kalorie.ui.login.LoginUiState
import com.indieme.kalorie.utils.AppUtil
import com.indieme.kalorie.utils.TimingUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(private val application: Application) : BaseViewModel(application) {

    private var userDTO: UserDTO? = null
    private var userProfileDTO: UserProfileDTO? = null


    // Backing property to avoid state updates from other classes
    private val _profileState: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(ProfileUiState.Empty)

    // The UI collects from this StateFlow to get its state updates
    val profileState: StateFlow<ProfileUiState> = _profileState
    private val accountRepository: AccountRepository =
        AccountRepository((application as KalorieApplication).database)

    init {

        getProfile()
    }

    private fun getProfile() {
        _profileState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                val result = accountRepository.getProfile()
                userDTO = result.response
                userProfileDTO = userDTO?.userProfile

                if (userProfileDTO == null) {
                    userProfileDTO =
                        UserProfileDTO(
                            null, null, null, null, null, null, TimingUtils.getTimeInString(
                                System.currentTimeMillis() / 1000,
                                TimingUtils.TimeFormats.CUSTOM_YYYY_MM_DD
                            ), null, null, null
                        );
                }


                _profileState.value = ProfileUiState.Success(result.response)
            } catch (e: Exception) {
                _profileState.value = ProfileUiState.Error(parseError(e))
            }


        }
    }

    fun saveProfile() {
        _profileState.value = ProfileUiState.Loading
        viewModelScope.launch {
            if (TextUtils.isEmpty(userProfileDTO?.targetDate)) {
                userProfileDTO?.targetDate = TimingUtils.getTimeInString(
                    System.currentTimeMillis() / 1000,
                    TimingUtils.TimeFormats.CUSTOM_YYYY_MM_DD
                )
            }




            userProfileDTO?.let {
                try {

                    userProfileDTO?.targetDate = userProfileDTO?.targetDate?.split(" ")?.first()

                    val result = accountRepository.updateProfile(it)
                    userProfileDTO = result.response
                    userDTO?.userProfile = userProfileDTO

                    if (result.status == 200) {
                        //Calculate BMR on success
                        val calculatedBMR = AppUtil.calculateBMR(
                            userProfileDTO?.gender,
                            userProfileDTO?.currentWeight?.toDouble(),
                            userProfileDTO?.currentHeight?.toDouble(),
                            userProfileDTO?.age?.toInt()
                        )
                        calculatedBMR?.let { bmr ->
                            LocalPreferenceManager.saveBMR(application, bmr.toString())
                            //Calculate TDEE
                            val calculatedTDEE = AppUtil.calculateTDEE(
                                calculatedBMR,
                                ActivityLevel.fromName(userProfileDTO?.activityLevel!!)
                            )
                            LocalPreferenceManager.saveTDEE(application, calculatedTDEE.toString())
                            //Calculate Daily Calorie Intake
                            val calculatedCalorieIntake = AppUtil.calculateDailyCalories(
                                calculatedTDEE,
                                GoalType.fromName(userProfileDTO?.goalType!!)
                            )

                            LocalPreferenceManager.saveDailyCalorieIntake(
                                application,
                                calculatedCalorieIntake.toString()
                            )


                            //Calculate Daily Calorie Intake
                            val calculatedBMI = AppUtil.calculateBMI(
                                userProfileDTO?.currentWeight!!.toDouble(),
                                AppUtil.convertInMeter(userProfileDTO?.currentHeight!!.toDouble())
                            )

                            LocalPreferenceManager.saveBMI(
                                application,
                                calculatedBMI.toString()
                            )
                        }


                    }
                    _profileState.value = ProfileUiState.Success(userDTO!!)

                } catch (e: Exception) {
                    _profileState.value = ProfileUiState.Error(parseError(e))
                }
            }


        }
    }

    // Function to update the gender
    fun updateAge(newAge: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(age = newAge)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

    // Function to update the gender
    fun updateTargetDate(newTargetDate: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(targetDate = newTargetDate)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

    // Function to update the gender
    fun updateGender(newGender: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(gender = newGender)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

    // Function to update the current weight
    fun updateCurrentWeight(newWeight: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(currentWeight = newWeight)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

    // Function to update the current weight
    fun updateGoalWeight(newWeight: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(targetWeight = newWeight)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

    // Function to update the current weight
    fun updateWeightUnit(newWeightUnit: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(weightUnit = newWeightUnit)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

    // Function to update the current weight
    fun updateHeightUnit(newHeightUnit: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(heightUnit = newHeightUnit)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

    // Function to update the current weight
    fun updateActivityLevel(newActivityLevel: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(activityLevel = newActivityLevel)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

    // Function to update the current weight
    fun updateCurrentHeight(newHeight: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(currentHeight = newHeight)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

    // Function to update the current weight
    fun updateGoalType(newGoalType: String) {
        userProfileDTO?.let { existingUserDTO ->
            // Update the existing UserDTO with the new weight
            val updatedUserProfileDTO = existingUserDTO.copy(goalType = newGoalType)
            userProfileDTO = updatedUserProfileDTO

            //
            val updatedUserDTO = userDTO?.copy(userProfile = userProfileDTO)
            userDTO = updatedUserDTO

            // Update the profile state with the new UserDTO
            if (_profileState.value is ProfileUiState.Success) {
                _profileState.value = ProfileUiState.Success(updatedUserDTO!!)
            }
        }
    }

}


// Represents different states for the Login screen
sealed class ProfileUiState {

    object Empty : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val userDTO: UserDTO) : ProfileUiState()
    data class Error(val exception: String?) : ProfileUiState()
}