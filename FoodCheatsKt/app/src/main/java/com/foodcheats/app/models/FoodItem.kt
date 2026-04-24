package com.foodcheats.app.models

data class FoodItem(
    val id: Int,
    val name: String,
    val category: String,
    val type: String, // "eat" or "cheat"
    val emoji: String,
    val description: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double,
    val healthScore: Int,
    val tasteScore: Int,
    val prepTime: String,
    val cuisine: String,
    val imageUrl: String,
    val moodTag: String,
    val ingredients: List<String>,
    val recipeSteps: List<String>,
    val dietTags: List<String>,
    val allergens: String
)
