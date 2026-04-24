package com.foodcheats.app.models

data class DietPlanItem(
    val mealTime: String,
    val mealTimeEmoji: String,
    val foodItem: FoodItem,
    val portion: String,
    val adjustedCalories: Int,
    val note: String
)
