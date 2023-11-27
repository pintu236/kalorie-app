package com.indieme.kalorie.data.model

enum class WeightUnit(var displayName: String) {
    KILOGRAM("kg"),
    POUND("lb"),
    GRAM("gm");

    companion object {
        fun fromDisplayName(name: String): WeightUnit {
            return WeightUnit.values().firstOrNull { it.displayName == name }
                ?: throw IllegalArgumentException("Invalid GoalType: $name")
        }

        fun fromName(type: String?): String? {
            return values().firstOrNull { it.name == type }?.displayName
                ?: type
        }
    }
}