package com.foodcheats.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.foodcheats.app.R
import com.foodcheats.app.database.FoodDatabase
import com.foodcheats.app.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_FOOD_ID = "food_id"
        fun newInstance(foodId: Int) = RecipeFragment().apply {
            arguments = Bundle().apply { putInt(ARG_FOOD_ID, foodId) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val foodId = arguments?.getInt(ARG_FOOD_ID, 1) ?: 1
        val food = FoodDatabase.getFoodById(foodId) ?: return

        binding.btnBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        Glide.with(this).load(food.imageUrl).placeholder(R.drawable.placeholder_food)
            .centerCrop().into(binding.ivRecipeImage)

        binding.tvRecipeName.text = "${food.emoji} ${food.name}"
        binding.tvPrepTime.text = "⏱️ ${food.prepTime}"
        binding.tvCaloriesRecipe.text = "🔥 ${food.calories} kcal"
        binding.tvCuisine.text = "🌍 ${food.cuisine}"

        addItemsToLayout(binding.layoutIngredients, food.ingredients, isBullet = true)
        addItemsToLayout(binding.layoutSteps, food.recipeSteps, isBullet = false)
    }

    private fun addItemsToLayout(layout: LinearLayout, items: List<String>, isBullet: Boolean) {
        layout.removeAllViews()
        items.forEachIndexed { index, text ->
            val tv = TextView(requireContext()).apply {
                textSize = 15f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                setPadding(0, 12, 0, 12)
                this.text = if (isBullet) "  🥄  $text" else "  ${index + 1}.  $text"
            }
            layout.addView(tv)
            val divider = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.divider))
            }
            layout.addView(divider)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
