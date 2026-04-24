package com.foodcheats.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodcheats.app.R
import com.foodcheats.app.databinding.ItemDietPlanBinding
import com.foodcheats.app.models.DietPlanItem

class DietPlanAdapter(
    private val items: List<DietPlanItem>,
    private val onClick: (DietPlanItem) -> Unit
) : RecyclerView.Adapter<DietPlanAdapter.VH>() {

    inner class VH(val binding: ItemDietPlanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemDietPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val b = holder.binding
        val ctx = holder.itemView.context

        Glide.with(ctx).load(item.foodItem.imageUrl).placeholder(R.drawable.placeholder_food)
            .centerCrop().into(b.ivMealFood)

        b.tvMealTime.text = "${item.mealTimeEmoji} ${item.mealTime}"
        b.tvFoodEmoji.text = item.foodItem.emoji
        b.tvFoodName.text = item.foodItem.name
        b.tvMealCalories.text = "${item.adjustedCalories} kcal"
        b.tvPortion.text = item.portion
        b.tvNote.text = "💡 ${item.note}"
        b.tvMacros.text = "P: ${item.foodItem.protein}g  C: ${item.foodItem.carbs}g  F: ${item.foodItem.fat}g"
        holder.itemView.setOnClickListener { onClick(item) }
    }
}
