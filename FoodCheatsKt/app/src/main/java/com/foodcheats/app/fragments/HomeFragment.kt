package com.foodcheats.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.foodcheats.app.MainActivity
import com.foodcheats.app.R
import com.foodcheats.app.adapters.FoodCardAdapter
import com.foodcheats.app.database.FoodDatabase
import com.foodcheats.app.databinding.FragmentHomeBinding
import com.foodcheats.app.utils.UserPreferences
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: UserPreferences
    private var currentMode = "eat"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = UserPreferences(requireContext())
        currentMode = prefs.getFoodMode()
        setupGreeting()
        setupModeToggle()
        loadFoods()
        setupQuickActions()
    }

    private fun setupGreeting() {
        val user = prefs.getUser()
        val name = user?.name ?: "Foodie"
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val (greeting, emoji) = when {
            hour < 12 -> "Good Morning" to "☀️"
            hour < 17 -> "Good Afternoon" to "🌤️"
            else -> "Good Evening" to "🌙"
        }
        binding.tvGreeting.text = "$emoji $greeting,"
        binding.tvUserName.text = "$name!"
        user?.let { binding.tvCalorieGoal.text = "Goal: ${it.dailyCalorieGoal} kcal" }
    }

    private fun setupModeToggle() {
        updateModeUI()
        binding.btnEat.setOnClickListener {
            currentMode = "eat"; prefs.setFoodMode("eat"); updateModeUI(); loadFoods()
        }
        binding.btnCheat.setOnClickListener {
            currentMode = "cheat"; prefs.setFoodMode("cheat"); updateModeUI(); loadFoods()
        }
    }

    private fun updateModeUI() {
        if (currentMode == "eat") {
            binding.btnEat.isSelected = true
            binding.btnCheat.isSelected = false
            binding.tvModeDesc.text = "🥦 Showing healthy, nourishing foods"
            binding.modeBanner.setBackgroundResource(R.drawable.bg_eat_banner)
        } else {
            binding.btnEat.isSelected = false
            binding.btnCheat.isSelected = true
            binding.tvModeDesc.text = "🍕 Showing tastier indulgent options"
            binding.modeBanner.setBackgroundResource(R.drawable.bg_cheat_banner)
        }
    }

    private fun loadFoods() {
        val foods = if (currentMode == "eat") FoodDatabase.getEatFoods() else FoodDatabase.getCheatFoods()
        val adapter = FoodCardAdapter(foods) { food ->
            val main = requireActivity() as MainActivity
            main.loadFragment(FoodDetailFragment.newInstance(food.id), showNav = true, addToBackStack = true)
        }
        binding.rvFoods.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFoods.adapter = adapter
    }

    private fun setupQuickActions() {
        binding.cardFeed.setOnClickListener {
            val main = requireActivity() as MainActivity
            main.loadFragment(FeedFragment(), showNav = true)
            main.selectNavItem(R.id.nav_feed)
        }
        binding.cardDietPlan.setOnClickListener {
            val main = requireActivity() as MainActivity
            main.loadFragment(DietPlanFragment(), showNav = true)
            main.selectNavItem(R.id.nav_diet_plan)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
