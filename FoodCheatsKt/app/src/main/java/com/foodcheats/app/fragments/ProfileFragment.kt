package com.foodcheats.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.foodcheats.app.MainActivity
import com.foodcheats.app.databinding.FragmentProfileBinding
import com.foodcheats.app.utils.UserPreferences

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = UserPreferences(requireContext())
        val user = prefs.getUser()

        user?.let {
            binding.tvProfileName.text = it.name
            binding.tvProfileInitial.text = it.name.firstOrNull()?.uppercase() ?: "?"
            binding.tvCurrentMode.text = "🍽️ Mode: ${prefs.getFoodMode().uppercase()}"
            val bmi = it.calculateBMI()
            binding.tvBmi.text = if (bmi > 0) String.format("%.1f", bmi) else "—"
            binding.tvBmiCategory.text = it.bmiCategory()
            binding.tvCalorieGoalProfile.text = "${it.dailyCalorieGoal} kcal/day"
            binding.tvTdee.text = "TDEE: ${it.calculateTDEE()} kcal"
            binding.tvBmr.text = "BMR: ${it.calculateBMR()} kcal"
            binding.tvProfileAge.text = "${it.age} yrs"
            binding.tvProfileWeight.text = "${it.weight} kg"
            binding.tvProfileHeight.text = "${it.height} cm"
            binding.tvProfileGoal.text = it.goal
            binding.tvProfileDiet.text = it.dietPreference
            binding.tvProfileActivity.text = it.activityLevel
            binding.tvProfileAllergies.text = it.allergies
        }

        binding.btnEditProfile.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(OnboardingFragment(), showNav = false, addToBackStack = true)
        }
        binding.btnLogout.setOnClickListener {
            prefs.logout()
            (requireActivity() as MainActivity).loadFragment(LoginFragment(), showNav = false)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
