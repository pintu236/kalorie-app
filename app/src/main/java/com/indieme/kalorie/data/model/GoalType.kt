package com.indieme.kalorie.data.model

enum class GoalType(var displayName: String, val factor: Double) {
    GainWeight("Gain Weight", 1.2),
    LoseWeight("Lose Weight", 0.8),
    MaintainWeight("Maintain Weight", 1.0),
    GainMuscle("Gain Muscle", 1.2),
    GainStrength("Gain Strength", 1.2),
    Endurance("Endurance", 1.1);


    companion object {
        fun fromDisplayName(name: String): GoalType {
            return values().firstOrNull { it.displayName == name }
                ?: throw IllegalArgumentException("Invalid GoalType: $name")
        }

        fun fromName(name: String): GoalType {
            return values().firstOrNull { it.name == name }
                ?: throw IllegalArgumentException("Invalid GoalType: $name")
        }

        fun getDisplayName(type: String?): String? {
            return values().firstOrNull { it.name == type }?.displayName
                ?: type
        }
    }

}
