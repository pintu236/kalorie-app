package com.indieme.kalorie.data.model

enum class ActivityLevel(var displayName: String, var factor: Double) {
    Sedentary("Sedentary", 1.2),
    LightlyActive("Lightly Active", 1.375),
    ModeratelyActive("Moderately Active", 1.55),
    VeryActive("Very Active", 1.725),
    SuperActive("Super Active", 1.9);


    companion object {
        fun fromDisplayName(name: String): ActivityLevel {
            return values().firstOrNull { it.displayName == name }
                ?: throw IllegalArgumentException("Invalid ActivityLevel: $name")
        }

        fun fromName(name: String): ActivityLevel {
            return values().firstOrNull { it.name == name }
                ?: throw IllegalArgumentException("Invalid ActivityLevel: $name")
        }

        fun getDisplayName(type: String?): String? {
            return values().firstOrNull { it.name == type }?.displayName
                ?: type
        }

    }
}