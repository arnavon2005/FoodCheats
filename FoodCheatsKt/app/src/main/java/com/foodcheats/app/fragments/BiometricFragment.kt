package com.foodcheats.app.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.foodcheats.app.MainActivity
import com.foodcheats.app.R
import com.foodcheats.app.databinding.FragmentBiometricBinding
import com.foodcheats.app.utils.BiometricHelper
import com.foodcheats.app.utils.UserPreferences

class BiometricFragment : Fragment() {

    private var _binding: FragmentBiometricBinding? = null
    private val binding get() = _binding!!

    // Flag so the prompt only auto-fires once on first resume
    private var hasPromptedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBiometricBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = UserPreferences(requireContext())
        val user = prefs.getUser()
        binding.tvWelcomeBack.text = "Welcome back,\n${user?.name ?: "Foodie"} 👋"

        // Manual retry button — always works
        binding.btnUnlock.setOnClickListener {
            triggerBiometric()
        }

        binding.tvSkip.setOnClickListener {
            navigateToHome()
        }
    }

    override fun onResume() {
        super.onResume()
        // Auto-trigger only on the FIRST resume
        // Delay gives the window time to fully attach (fixes OEM timing issues)
        if (!hasPromptedOnce) {
            hasPromptedOnce = true
            Handler(Looper.getMainLooper()).postDelayed({
                if (isAdded && !isDetached) {
                    triggerBiometric()
                }
            }, 300) // 300ms is enough for window to settle
        }
    }

    private fun triggerBiometric() {
        val helper = BiometricHelper(
            activity = requireActivity() as androidx.fragment.app.FragmentActivity,
            onSuccess = {
                if (isAdded) navigateToHome()
            },
            onFailure = { error ->
                if (isAdded) {
                    Toast.makeText(requireContext(),
                        "Authentication error: $error", Toast.LENGTH_SHORT).show()
                }
            },
            onCancel = {
                // User deliberately pressed Cancel/Back — close the app
                requireActivity().finish()
            }
        )

        if (helper.canAuthenticate()) {
            helper.showPrompt()
        } else {
            // No biometrics enrolled on this device
            Toast.makeText(
                requireContext(),
                "No biometrics set up. Go to Settings → Security → Fingerprint.",
                Toast.LENGTH_LONG
            ).show()
            // Wait 2 seconds then go home so they can read the message
            Handler(Looper.getMainLooper()).postDelayed({
                if (isAdded) navigateToHome()
            }, 2000)
        }
    }

    private fun navigateToHome() {
        val main = requireActivity() as MainActivity
        main.loadFragment(HomeFragment(), showNav = true)
        main.selectNavItem(R.id.nav_home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}