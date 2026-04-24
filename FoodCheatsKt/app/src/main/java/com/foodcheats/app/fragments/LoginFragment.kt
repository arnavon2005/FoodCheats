package com.foodcheats.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.foodcheats.app.MainActivity
import com.foodcheats.app.databinding.FragmentLoginBinding
import com.foodcheats.app.models.UserProfile
import com.foodcheats.app.utils.UserPreferences

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = UserPreferences(requireContext())

        binding.btnGetStarted.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            if (name.isEmpty()) { binding.etName.error = "Enter your name"; return@setOnClickListener }
            if (email.isEmpty()) { binding.etEmail.error = "Enter your email"; return@setOnClickListener }

            val user = UserProfile(name = name)
            prefs.saveUser(user)
            (requireActivity() as MainActivity).loadFragment(OnboardingFragment(), showNav = false)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
