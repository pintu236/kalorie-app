package com.indieme.kalorie.data.model

enum class HeightUnit(var displayName: String) {
    METER("m"),
    CENTIMETER("cm"),
    INCH("in");

    companion object {
        fun fromDisplayName(name: String): HeightUnit {
            return HeightUnit.values().firstOrNull { it.displayName == name }
                ?: throw IllegalArgumentException("Invalid GoalType: $name")
        }

        fun fromName(type: String?): String? {
            return values().firstOrNull { it.name == type }?.displayName
                ?: type
        }
    }
}