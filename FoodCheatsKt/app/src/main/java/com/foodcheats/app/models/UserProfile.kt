package com.foodcheats.app.models

data class UserProfile(
    var name: String = "",
    var age: Int = 0,
    var weight: Float = 0f,
    var height: Float = 0f,
    var gender: String = "male",
    var activityLevel: String = "sedentary",
    var goal: String = "Maintain Weight",
    var dailyCalorieGoal: Int = 2000,
    var dietPreference: String = "non-vegetarian",
    var allergies: String = "None",
    var moodToday: String = "happy"
) {
    fun calculateBMR(): Int {
        return if (gender.equals("female", ignoreCase = true)) {
            (10 * weight + 6.25 * height - 5 * age - 161).toInt()
        } else {
            (10 * weight + 6.25 * height - 5 * age + 5).toInt()
        }
    }

    fun calculateTDEE(): Int {
        val bmr = calculateBMR().toDouble()
        val multiplier = when (activityLevel) {
            "light" -> 1.375
            "moderate" -> 1.55
            "active" -> 1.725
            "very_active" -> 1.9
            else -> 1.2
        }
        return (bmr * multiplier).toInt()
    }

    fun calculateBMI(): Float {
        if (height <= 0 || weight <= 0) return 0f
        val heightM = height / 100f
        return weight / (heightM * heightM)
    }

    fun bmiCategory(): String {
        val bmi = calculateBMI()
        return when {
            bmi < 18.5f -> "Underweight"
            bmi < 25f -> "Normal ✅"
            bmi < 30f -> "Overweight"
            else -> "Obese"
        }
    }
}
