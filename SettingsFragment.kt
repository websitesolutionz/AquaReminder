package com.aquareminder.app.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aquareminder.app.AquaReminderApp
import com.aquareminder.app.R
import com.aquareminder.app.databinding.FragmentSettingsBinding
import com.google.android.material.slider.Slider
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var prefsManager: com.aquareminder.app.data.PreferencesManager
    private var currentGoal = 2000
    private var currentGlassSize = 250
    private var isPremium = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val app = requireActivity().application as AquaReminderApp
        prefsManager = app.preferencesManager
        
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.getUserProfile().collect { profile ->
                currentGoal = profile.dailyGoal
                currentGlassSize = profile.glassSize
                isPremium = profile.isPremium
                
                binding.tvCurrentGoal.text = "$currentGoal ml"
                binding.tvCurrentGlass.text = "$currentGlassSize ml"
                binding.switchDarkMode.isChecked = profile.darkModeEnabled
                
                // Show/hide premium card
                if (isPremium) {
                    binding.cardPremium.visibility = View.GONE
                }
            }
        }
    }

    private fun setupListeners() {
        binding.cardDailyGoal.setOnClickListener {
            showDailyGoalDialog()
        }

        binding.cardGlassSize.setOnClickListener {
            showGlassSizeDialog()
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
        }

        binding.btnGoPremium.setOnClickListener {
            showPremiumDialog()
        }
    }

    private fun showDailyGoalDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_slider, null)
        val slider = dialogView.findViewById<Slider>(R.id.slider)
        
        slider.valueFrom = 1000f
        slider.valueTo = 5000f
        slider.stepSize = 100f
        slider.value = currentGoal.toFloat()
        
        AlertDialog.Builder(requireContext())
            .setTitle("Set Daily Goal")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newGoal = slider.value.toInt()
                saveGoal(newGoal)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showGlassSizeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_number_input, null)
        val editText = dialogView.findViewById<EditText>(R.id.etNumber)
        
        editText.setText(currentGlassSize.toString())
        
        AlertDialog.Builder(requireContext())
            .setTitle("Set Glass Size (ml)")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newSize = editText.text.toString().toIntOrNull()
                if (newSize != null && newSize > 0) {
                    saveGlassSize(newSize)
                } else {
                    Toast.makeText(requireContext(), "Invalid value", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showPremiumDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.premium_title))
            .setMessage(
                "Premium Features:\n\n" +
                "• ${getString(R.string.premium_feature_1)}\n" +
                "• ${getString(R.string.premium_feature_2)}\n" +
                "• ${getString(R.string.premium_feature_3)}\n" +
                "• ${getString(R.string.premium_feature_4)}\n\n" +
                "Choose your plan:\n" +
                "${getString(R.string.premium_price_monthly)}\n" +
                "${getString(R.string.premium_price_lifetime)}"
            )
            .setPositiveButton("Buy Lifetime") { _, _ ->
                // TODO: Implement billing
                activatePremium()
            }
            .setNeutralButton("Subscribe Monthly") { _, _ ->
                // TODO: Implement billing
                activatePremium()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveGoal(goal: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.updateDailyGoal(goal)
            Toast.makeText(requireContext(), "Goal updated!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveGlassSize(size: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.updateGlassSize(size)
            Toast.makeText(requireContext(), "Glass size updated!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDarkMode(enabled: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.setDarkMode(enabled)
            
            // Apply dark mode
            if (enabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun activatePremium() {
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.setPremium(true)
            Toast.makeText(requireContext(), "Premium activated! 🎉", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
