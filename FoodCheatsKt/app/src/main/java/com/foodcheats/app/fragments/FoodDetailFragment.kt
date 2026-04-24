package com.foodcheats.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.foodcheats.app.MainActivity
import com.foodcheats.app.R
import com.foodcheats.app.database.FoodDatabase
import com.foodcheats.app.databinding.FragmentFoodDetailBinding

class FoodDetailFragment : Fragment() {

    private var _binding: FragmentFoodDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_FOOD_ID = "food_id"
        fun newInstance(foodId: Int) = FoodDetailFragment().apply {
            arguments = Bundle().apply { putInt(ARG_FOOD_ID, foodId) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val foodId = arguments?.getInt(ARG_FOOD_ID, 1) ?: 1
        val food = FoodDatabase.getFoodById(foodId) ?: return

        Glide.with(this).load(food.imageUrl).placeholder(R.drawable.placeholder_food)
            .centerCrop().into(binding.ivFoodImage)

        binding.tvFoodEmoji.text = food.emoji
        binding.tvFoodName.text = food.name
        binding.tvFoodCategory.text = "${food.category} · ${food.cuisine}"
        binding.tvDescription.text = food.description
        binding.tvCalories.text = "${food.calories}"
        binding.tvProtein.text = "${food.protein}g"
        binding.tvCarbs.text = "${food.carbs}g"
        binding.tvFat.text = "${food.fat}g"
        binding.tvFiber.text = "${food.fiber}g"
        binding.tvPrepTime.text = food.prepTime
        binding.tvHealthScore.text = "${food.healthScore}/10"
        binding.tvTasteScore.text = "${food.tasteScore}/10"
        binding.tvTypeBadge.text = if (food.type == "eat") "✅ EAT" else "🔥 CHEAT"
        binding.tvTypeBadge.setBackgroundResource(
            if (food.type == "eat") R.drawable.bg_badge_eat else R.drawable.bg_badge_cheat)
        binding.tvDietTags.text = food.dietTags.joinToString("  ") { "• $it" }
        binding.tvAllergens.text = "⚠️ ${food.allergens}"
        binding.tvMoodTag.text = "😊 Best for: ${food.moodTag.replace(",", " · ")}"

        binding.btnBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        binding.btnViewRecipe.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(
                RecipeFragment.newInstance(food.id), showNav = true, addToBackStack = true)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
