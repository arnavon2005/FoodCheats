package com.foodcheats.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.foodcheats.app.adapters.FeedPagerAdapter
import com.foodcheats.app.database.FoodDatabase
import com.foodcheats.app.databinding.FragmentFeedBinding
import com.foodcheats.app.utils.UserPreferences

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = UserPreferences(requireContext())
        val mode = prefs.getFoodMode()

        val foods = (if (mode == "eat") FoodDatabase.getEatFoods() else FoodDatabase.getCheatFoods())
            .shuffled().toMutableList()

        val adapter = FeedPagerAdapter(foods, requireActivity())
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewPager.offscreenPageLimit = 2

        if (mode == "eat") binding.tabEat.isSelected = true else binding.tabCheat.isSelected = true

        binding.tabEat.setOnClickListener {
            prefs.setFoodMode("eat")
            adapter.updateData(FoodDatabase.getEatFoods().shuffled())
            binding.tabEat.isSelected = true; binding.tabCheat.isSelected = false
        }
        binding.tabCheat.setOnClickListener {
            prefs.setFoodMode("cheat")
            adapter.updateData(FoodDatabase.getCheatFoods().shuffled())
            binding.tabEat.isSelected = false; binding.tabCheat.isSelected = true
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
