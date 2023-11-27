package com.indieme.kalorie.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.stringResource
import com.indieme.kalorie.data.model.ActivityLevel
import com.indieme.kalorie.data.model.FoodNutrientsDTO
import com.indieme.kalorie.data.model.GoalType
import com.indieme.kalorie.data.model.IntPickerDTO
import com.indieme.kalorie.data.model.NumberPickerDTO
import com.indieme.kalorie.data.model.NutrientsDTO
import com.indieme.kalorie.data.model.StringPickerDTO

object AppUtil {

    public fun showMessage(context: Context, message: String?) {

        Toast.makeText(context, message ?: "", Toast.LENGTH_LONG).show()
    }

    fun findNutrientById(id: Int, nutrientsDTO: List<FoodNutrientsDTO>?): FoodNutrientsDTO? {
        return nutrientsDTO?.first {
            it.nutrientId == id
        }
    }

    fun getMealNameById(selectedMeal: Int): String {
        return when (selectedMeal) {
            MEAL_BREAKFAST -> "Breakfast"
            MEAL_LUNCH -> "Lunch"
            MEAL_DINNER -> "Dinner"
            MEAL_SNACKS -> "Snacks"
            else -> {
                "Breakfast"
            }
        }
    }

    fun generateAge(): List<IntPickerDTO> {
        val ageList = mutableListOf<Int>()
        var current = 10
        val step =
            1 // Adjust the step to control the precision (e.g., 0.01 for two decimal places)

        while (current <= 100) {
            ageList.add(current)
            current += step
        }

        return ageList.map { IntPickerDTO(it) }
    }

    fun generateMetricNumberList(): List<NumberPickerDTO> {
        val decimalList = mutableListOf<Double>()
        var current = 30.0
        val step =
            0.1 // Adjust the step to control the precision (e.g., 0.01 for two decimal places)

        while (current <= 200) {
            decimalList.add(current)
            current += step
        }

        return decimalList.map { NumberPickerDTO(it) }
    }

    fun getGenderList(): List<StringPickerDTO> {
        val gender = listOf("M", "F", "Others")
        return gender.map { StringPickerDTO(it) }
    }

    fun convertInPounds(weightInKg: Double): Double {
        return 2.20462 * weightInKg;
    }

    fun convertInCm(heightInInch: Double): Double {
        return 2.54 * heightInInch;
    }

    fun convertInMeter(heightInCm: Double): Double {
        return heightInCm / 100;
    }

    /**
     * Calculate Body Mass Index
     */
    fun calculateBMI(weightInKg: Double, heightInM: Double): Double {
        return weightInKg / (heightInM * heightInM)
    }

    /**
     * Calculate The Basal Metabolic Rate (BMR) using
     * one of the most widely used formulas is the Mifflin-St Jeor equation
     */
    fun calculateBMR(
        gender: String?,
        weightInKg: Double?,
        heightInCm: Double?,
        ageInYears: Int?
    ): Double? {
        if (gender == null || weightInKg == null || heightInCm == null || ageInYears == null) {
            return null;
        }
        return if (gender == "M") {
            (10 * weightInKg) + (6.25 * heightInCm) - (5 * ageInYears) + 5
        } else {
            (10 * weightInKg) + (6.25 * heightInCm) - (5 * ageInYears) - 161
        }


    }

    /**
     * Calculate TDEE based on activity level
     */
    fun calculateTDEE(bmr: Double, activityLevel: ActivityLevel): Double {
        return bmr * activityLevel.factor
    }

    /**
     *
     */
    fun calculateDailyCalories(tdee: Double, goalType: GoalType): Double {
        return tdee * goalType.factor
    }

    fun interpretBMI(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi >= 18.5 && bmi < 24.9 -> "Normal Weight"
            bmi >= 25 && bmi < 29.9 -> "Overweight"
            else -> "Obese"
        }
    }
}