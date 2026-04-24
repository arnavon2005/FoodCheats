package com.foodcheats.app.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.foodcheats.app.MainActivity
import com.foodcheats.app.R
import com.foodcheats.app.databinding.FragmentSplashBinding
import com.foodcheats.app.utils.UserPreferences

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fadeIn = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
        fadeIn.duration = 900
        binding.logoIcon.startAnimation(fadeIn)
        binding.appName.startAnimation(fadeIn)
        binding.tagline.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            if (!isAdded) return@postDelayed
            val prefs = UserPreferences(requireContext())
            val main = requireActivity() as MainActivity
            when {
                !prefs.isLoggedIn() ->
                    main.loadFragment(LoginFragment(), showNav = false)
                !prefs.isSetupDone() ->
                    main.loadFragment(OnboardingFragment(), showNav = false)
                else -> {
                    if (prefs.isBiometricEnabled()) {
                        main.loadFragment(BiometricFragment(), showNav = false)
                    } else {
                        main.loadFragment(HomeFragment(), showNav = true)
                        main.selectNavItem(R.id.nav_home)
                    }
                }
            }
        }, 2800)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
