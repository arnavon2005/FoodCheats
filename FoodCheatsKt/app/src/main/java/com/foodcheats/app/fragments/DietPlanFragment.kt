package com.foodcheats.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodcheats.app.MainActivity
import com.foodcheats.app.adapters.DietPlanAdapter
import com.foodcheats.app.database.FoodDatabase
import com.foodcheats.app.databinding.FragmentDietPlanBinding
import com.foodcheats.app.models.DietPlanItem
import com.foodcheats.app.models.FoodItem
import com.foodcheats.app.utils.UserPreferences
import kotlin.random.Random

class DietPlanFragment : Fragment() {

    private var _binding: FragmentDietPlanBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: UserPreferences
    private var selectedMood = "happy"

    private val moods = listOf("happy", "stressed", "tired", "focused", "energetic", "comfort", "sick")
    private val moodEmojis = listOf("😊", "😤", "😴", "🧠", "⚡", "🤗", "🤒")
    private val moodLabels = listOf("Happy", "Stressed", "Tired", "Focused", "Energetic", "Comfort", "Sick")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDietPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = UserPreferences(requireContext())
        setupMoodChips()
        generatePlan()
        binding.btnRegeneratePlan.setOnClickListener {
            generatePlan()
            Toast.makeText(requireContext(), "✨ New plan generated!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupMoodChips() {
        val chips = listOf(binding.chipHappy, binding.chipStressed, binding.chipTired,
            binding.chipFocused, binding.chipEnergetic, binding.chipComfort, binding.chipSick)

        chips.forEachIndexed { i, chip ->
            chip.setOnClickListener {
                selectedMood = moods[i]
                chips.forEach { it.isSelected = false }
                chip.isSelected = true
                binding.tvSelectedMood.text = "${moodEmojis[i]} Feeling ${moodLabels[i]}"
                generatePlan()
            }
        }
        binding.chipHappy.isSelected = true
        binding.tvSelectedMood.text = "😊 Feeling Happy"
    }

    private fun generatePlan() {
        val user = prefs.getUser()
        val calorieGoal = if ((user?.dailyCalorieGoal ?: 0) > 0) user!!.dailyCalorieGoal else 2000
        val mode = prefs.getFoodMode()
        val pool = FoodDatabase.getFoodsByMood(selectedMood, mode)

        val plan = mutableListOf<DietPlanItem>()
        val bfCal = (calorieGoal * 0.25).toInt()
        val lCal = (calorieGoal * 0.35).toInt()
        val snCal = (calorieGoal * 0.10).toInt()
        val dCal = (calorieGoal * 0.30).toInt()

        randomNear(pool, bfCal)?.let { plan.add(DietPlanItem("Breakfast", "☀️", it, "1 serving", bfCal, "Start your day right!")) }
        randomNear(pool, lCal)?.let { plan.add(DietPlanItem("Lunch", "🌤️", it, "1 serving", lCal, "Keep energy levels up!")) }
        randomNear(pool, snCal)?.let { plan.add(DietPlanItem("Snack", "🍎", it, "Half serving", snCal, "Beat the 4pm slump!")) }
        randomNear(pool, dCal)?.let { plan.add(DietPlanItem("Dinner", "🌙", it, "1 serving", dCal, "Light and satisfying!")) }

        val totalCal = plan.sumOf { it.adjustedCalories }
        binding.tvTotalCalories.text = "$totalCal / $calorieGoal kcal"
        binding.tvCalorieGoal.text = "Goal: $calorieGoal kcal"
        binding.progressCalories.progress = minOf(100, (totalCal * 100.0 / calorieGoal).toInt())

        val adapter = DietPlanAdapter(plan) { item ->
            (requireActivity() as MainActivity).loadFragment(
                FoodDetailFragment.newInstance(item.foodItem.id), showNav = true, addToBackStack = true)
        }
        binding.rvDietPlan.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDietPlan.adapter = adapter
    }

    private fun randomNear(pool: List<FoodItem>, target: Int): FoodItem? {
        if (pool.isEmpty()) return null
        val candidates = pool.filter { Math.abs(it.calories - target) < 220 }.ifEmpty { pool }
        return candidates[Random.nextInt(candidates.size)]
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
