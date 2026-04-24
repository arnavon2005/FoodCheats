package com.foodcheats.app.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodcheats.app.MainActivity
import com.foodcheats.app.R
import com.foodcheats.app.databinding.ItemFeedCardBinding
import com.foodcheats.app.fragments.FoodDetailFragment
import com.foodcheats.app.fragments.RecipeFragment
import com.foodcheats.app.models.FoodItem

class FeedPagerAdapter(
    private var foods: List<FoodItem>,
    private val activity: Activity
) : RecyclerView.Adapter<FeedPagerAdapter.VH>() {

    fun updateData(newFoods: List<FoodItem>) { foods = newFoods; notifyDataSetChanged() }

    inner class VH(val binding: ItemFeedCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemFeedCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = foods.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val food = foods[position]
        val b = holder.binding
        val ctx = holder.itemView.context

        Glide.with(ctx).load(food.imageUrl).placeholder(R.drawable.placeholder_food)
            .centerCrop().into(b.ivBackground)

        b.tvFeedEmoji.text = food.emoji
        b.tvFeedName.text = food.name
        b.tvFeedDescription.text = food.description
        b.tvFeedCalories.text = "🔥 ${food.calories} kcal"
        b.tvFeedProtein.text = "💪 ${food.protein}g protein"
        b.tvFeedPrepTime.text = "⏱️ ${food.prepTime}"
        b.tvFeedCuisine.text = "🌍 ${food.cuisine}"
        b.tvHealthScore.text = "Health: ${food.healthScore}/10"
        b.tvTasteScore.text = "Taste: ${food.tasteScore}/10"
        b.tvTypeBadge.text = if (food.type == "eat") "✅ EAT" else "🔥 CHEAT"
        b.tvTypeBadge.setBackgroundResource(
            if (food.type == "eat") R.drawable.bg_badge_eat else R.drawable.bg_badge_cheat)

        b.btnViewDetails.setOnClickListener {
            (activity as MainActivity).loadFragment(
                FoodDetailFragment.newInstance(food.id), showNav = true, addToBackStack = true)
        }
        b.btnViewRecipe.setOnClickListener {
            (activity as MainActivity).loadFragment(
                RecipeFragment.newInstance(food.id), showNav = true, addToBackStack = true)
        }
    }
}
