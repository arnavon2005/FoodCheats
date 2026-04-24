package com.foodcheats.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.foodcheats.app.databinding.ActivityMainBinding
import com.foodcheats.app.fragments.*
import com.foodcheats.app.utils.UserPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(SplashFragment(), showNav = false)
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { loadFragment(HomeFragment(), showNav = true); true }
                R.id.nav_feed -> { loadFragment(FeedFragment(), showNav = true); true }
                R.id.nav_diet_plan -> { loadFragment(DietPlanFragment(), showNav = true); true }
                R.id.nav_profile -> { loadFragment(ProfileFragment(), showNav = true); true }
                else -> false
            }
        }
    }

    fun loadFragment(fragment: Fragment, showNav: Boolean = true, addToBackStack: Boolean = false) {
        binding.bottomNav.visibility = if (showNav) View.VISIBLE else View.GONE
        val tx = supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragmentContainer, fragment)
        if (addToBackStack) tx.addToBackStack(null)
        tx.commit()
    }

    fun showNav(show: Boolean) {
        binding.bottomNav.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun selectNavItem(itemId: Int) {
        binding.bottomNav.selectedItemId = itemId
    }
}
