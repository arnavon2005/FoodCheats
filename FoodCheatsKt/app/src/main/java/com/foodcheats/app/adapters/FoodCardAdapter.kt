package com.foodcheats.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodcheats.app.R
import com.foodcheats.app.databinding.ItemFoodCardBinding
import com.foodcheats.app.models.FoodItem

class FoodCardAdapter(
    private val foods: List<FoodItem>,
    private val onClick: (FoodItem) -> Unit
) : RecyclerView.Adapter<FoodCardAdapter.VH>() {

    inner class VH(val binding: ItemFoodCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemFoodCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = foods.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val food = foods[position]
        val b = holder.binding
        val ctx = holder.itemView.context

        Glide.with(ctx).load(food.imageUrl).placeholder(R.drawable.placeholder_food)
            .centerCrop().into(b.ivFood)

        b.tvEmoji.text = food.emoji
        b.tvFoodName.text = food.name
        b.tvCalories.text = "${food.calories} kcal"
        b.tvCategory.text = food.category
        b.tvHealthScore.text = "❤️ ${food.healthScore}/10"
        b.tvBadge.text = if (food.type == "eat") "EAT ✅" else "CHEAT 🔥"
        b.tvBadge.setBackgroundResource(
            if (food.type == "eat") R.drawable.bg_badge_eat else R.drawable.bg_badge_cheat)
        holder.itemView.setOnClickListener { onClick(food) }
    }
}
