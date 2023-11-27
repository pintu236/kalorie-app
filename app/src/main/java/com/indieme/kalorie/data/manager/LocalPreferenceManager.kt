package com.indieme.kalorie.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.indieme.kalorie.data.model.UserDTO
import com.indieme.kalorie.utils.K_AGE
import com.indieme.kalorie.utils.K_BMI
import com.indieme.kalorie.utils.K_BMR
import com.indieme.kalorie.utils.K_CONSUMED_CALORIE
import com.indieme.kalorie.utils.K_CURRENT_WEIGHT
import com.indieme.kalorie.utils.K_DALIY_CALORIE
import com.indieme.kalorie.utils.K_EMAIL
import com.indieme.kalorie.utils.K_GENDER
import com.indieme.kalorie.utils.K_HEIGHT
import com.indieme.kalorie.utils.K_ID
import com.indieme.kalorie.utils.K_REMAINING_CALORIE
import com.indieme.kalorie.utils.K_TDEE
import com.indieme.kalorie.utils.K_TOKEN
import com.indieme.kalorie.utils.K_USERNAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

object LocalPreferenceManager {
    private val prefBMR = stringPreferencesKey(K_BMR)
    private val prefTDEE = stringPreferencesKey(K_TDEE)
    private val prefDailyCalorie = stringPreferencesKey(K_DALIY_CALORIE)
    private val prefBMI  = stringPreferencesKey(K_BMI)
    private val prefDailyConsumedCalorie = stringPreferencesKey(K_CONSUMED_CALORIE)
    private val prefGender = stringPreferencesKey(K_GENDER)
    private val prefAge = stringPreferencesKey(K_AGE)
    private val prefHeight = stringPreferencesKey(K_HEIGHT)
    private val prefCurrentWeight = stringPreferencesKey(K_CURRENT_WEIGHT)
    private val prefId = intPreferencesKey(K_ID)
    private val prefEmail = stringPreferencesKey(K_EMAIL)
    private val prefUsername = stringPreferencesKey(K_USERNAME)
    private val prefToken = stringPreferencesKey(K_TOKEN)


    // At the top level of your kotlin file:
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    fun readBMI(context: Context): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                // Get our show completed value, defaulting to false if not set:
                val calculatedBMR = preferences[prefBMI] ?: ""
                calculatedBMR
            }
    }

    fun readDailyConsumedCalorie(context: Context): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                // Get our show completed value, defaulting to false if not set:
                val calculatedBMR = preferences[prefDailyConsumedCalorie] ?: "0"
                calculatedBMR
            }
    }


    fun readBMR(context: Context): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                // Get our show completed value, defaulting to false if not set:
                val calculatedBMR = preferences[prefBMR] ?: ""
                calculatedBMR
            }
    }


    fun readTDEE(context: Context): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                // Get our show completed value, defaulting to false if not set:
                val calculatedBMR = preferences[prefTDEE] ?: ""
                calculatedBMR
            }
    }

    fun readDailyCalorie(context: Context): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                // Get our show completed value, defaulting to false if not set:
                val calculatedBMR = preferences[prefDailyCalorie] ?: "0"
                calculatedBMR
            }
    }

    suspend fun readUserData(context: Context): UserDTO? {
        return context.dataStore.data
            .map { preferences ->
                // No type safety.
                UserDTO(
                    preferences[prefId] ?: 0,
                    preferences[prefUsername] ?: "",
                    preferences[prefEmail] ?: "",
                    preferences[prefToken]
                        ?: "",
                    preferences[prefBMR] ?: "",
                    preferences[prefTDEE] ?: "",
                    preferences[prefDailyCalorie] ?: "",
                    null,
                    listOf()

                )
            }.firstOrNull()

    }

    suspend fun saveUserData(context: Context, userDTO: UserDTO) {
        context.dataStore.edit { settings ->
            settings[prefId] = userDTO.id
            settings[prefEmail] = userDTO.email
            settings[prefUsername] = userDTO.username
            settings[prefToken] = userDTO.token ?: ""
        }
    }

    suspend fun saveBMR(context: Context, bmr: String) {
        //Update Cache
        LocalAppMemCache.userBMR = bmr;
        context.dataStore.edit { settings ->
            settings[prefBMR] = bmr
        }
    }

    suspend fun saveTDEE(context: Context, tdee: String) {
        //Update Cache
        LocalAppMemCache.userTDEE = tdee;
        context.dataStore.edit { settings ->
            settings[prefTDEE] = tdee
        }
    }

    suspend fun saveDailyCalorieIntake(context: Context, calIntake: String) {
        //Update Cache
        LocalAppMemCache.dailyCalorie = calIntake;
        context.dataStore.edit { settings ->
            settings[prefDailyCalorie] = calIntake
        }
    }


    suspend fun saveDailyConsumedCalorie(context: Context, calIntake: String) {
        context.dataStore.edit { settings ->
            settings[prefDailyConsumedCalorie] = calIntake
        }
    }

    suspend fun saveBMI(context: Context, bmi: String) {
        //Update Cache
        LocalAppMemCache.bmi = bmi;
        context.dataStore.edit { settings ->
            settings[prefBMI] = bmi
        }
    }


    suspend fun removeUserData(context: Context) {
        context.dataStore.edit {
            it.clear()
        }
    }

}