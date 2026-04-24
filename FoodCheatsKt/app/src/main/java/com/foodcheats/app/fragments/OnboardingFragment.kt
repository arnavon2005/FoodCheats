package com.foodcheats.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.foodcheats.app.MainActivity
import com.foodcheats.app.R
import com.foodcheats.app.databinding.FragmentOnboardingBinding
import com.foodcheats.app.models.UserProfile
import com.foodcheats.app.utils.UserPreferences

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = UserPreferences(requireContext())

        ArrayAdapter.createFromResource(requireContext(), R.array.genders, android.R.layout.simple_spinner_dropdown_item)
            .also { binding.spinnerGender.adapter = it }
        ArrayAdapter.createFromResource(requireContext(), R.array.activity_levels, android.R.layout.simple_spinner_dropdown_item)
            .also { binding.spinnerActivity.adapter = it }
        ArrayAdapter.createFromResource(requireContext(), R.array.goals, android.R.layout.simple_spinner_dropdown_item)
            .also { binding.spinnerGoal.adapter = it }
        ArrayAdapter.createFromResource(requireContext(), R.array.diet_prefs, android.R.layout.simple_spinner_dropdown_item)
            .also { binding.spinnerDiet.adapter = it }

        val existing = prefs.getUser()
        if (!existing?.name.isNullOrEmpty()) {
            binding.tvWelcomeName.text = "Hey ${existing!!.name}! Tell us more 🍽️"
        }

        binding.btnSaveProfile.setOnClickListener { saveProfile(prefs) }
    }

    private fun saveProfile(prefs: UserPreferences) {
        val ageStr = binding.etAge.text.toString().trim()
        val weightStr = binding.etWeight.text.toString().trim()
        val heightStr = binding.etHeight.text.toString().trim()

        if (ageStr.isEmpty()) { binding.etAge.error = "Required"; return }
        if (weightStr.isEmpty()) { binding.etWeight.error = "Required"; return }
        if (heightStr.isEmpty()) { binding.etHeight.error = "Required"; return }

        val user = prefs.getUser() ?: UserProfile()
        user.age = ageStr.toInt()
        user.weight = weightStr.toFloat()
        user.height = heightStr.toFloat()
        user.gender = if (binding.spinnerGender.selectedItemPosition == 1) "female" else "male"
        user.activityLevel = when (binding.spinnerActivity.selectedItemPosition) {
            1 -> "light"; 2 -> "moderate"; 3 -> "active"; 4 -> "very_active"; else -> "sedentary"
        }
        user.goal = binding.spinnerGoal.selectedItem.toString()
        user.dietPreference = binding.spinnerDiet.selectedItem.toString().lowercase()
        user.dailyCalorieGoal = user.calculateTDEE()
        val allergies = binding.etAllergies.text.toString().trim()
        user.allergies = allergies.ifEmpty { "None" }

        prefs.saveUser(user)
        prefs.setBiometricEnabled(binding.switchBiometric.isChecked)
        prefs.setSetupDone(true)

        val main = requireActivity() as MainActivity
        if (binding.switchBiometric.isChecked) {
            main.loadFragment(BiometricFragment(), showNav = false)
        } else {
            main.loadFragment(HomeFragment(), showNav = true)
            main.selectNavItem(R.id.nav_home)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
