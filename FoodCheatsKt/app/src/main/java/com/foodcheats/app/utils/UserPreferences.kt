package com.foodcheats.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.foodcheats.app.models.UserProfile
import com.google.gson.Gson

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREF_NAME = "FoodCheatsUser"
        private const val KEY_USER = "user_profile"
        private const val KEY_LOGGED_IN = "is_logged_in"
        private const val KEY_MODE = "food_mode"
        private const val KEY_SETUP_DONE = "setup_done"
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
    }

    fun saveUser(user: UserProfile) {
        prefs.edit()
            .putString(KEY_USER, gson.toJson(user))
            .putBoolean(KEY_LOGGED_IN, true)
            .apply()
    }

    fun getUser(): UserProfile? {
        val json = prefs.getString(KEY_USER, null) ?: return null
        return gson.fromJson(json, UserProfile::class.java)
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_LOGGED_IN, false)

    fun isSetupDone(): Boolean = prefs.getBoolean(KEY_SETUP_DONE, false)

    fun setSetupDone(done: Boolean) = prefs.edit().putBoolean(KEY_SETUP_DONE, done).apply()

    fun setBiometricEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, enabled).apply()

    fun isBiometricEnabled(): Boolean = prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false)

    fun setFoodMode(mode: String) = prefs.edit().putString(KEY_MODE, mode).apply()

    fun getFoodMode(): String = prefs.getString(KEY_MODE, "eat") ?: "eat"

    fun logout() = prefs.edit().putBoolean(KEY_LOGGED_IN, false).apply()
}
