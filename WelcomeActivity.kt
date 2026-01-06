package com.aquareminder.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aquareminder.app.AquaReminderApp
import com.aquareminder.app.R
import com.aquareminder.app.databinding.ActivityWelcomeBinding
import com.aquareminder.app.model.UserProfile
import com.google.android.material.slider.Slider
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private var dailyGoal = 2000
    private var glassSize = 250

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // Daily goal slider
        binding.sliderDailyGoal.addOnChangeListener { slider, value, fromUser ->
            dailyGoal = value.toInt()
            binding.tvDailyGoalValue.text = "$dailyGoal ml"
        }

        // Glass size radio buttons
        binding.radioGroupGlassSize.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio250 -> {
                    glassSize = 250
                    binding.tilCustomGlass.visibility = View.GONE
                }
                R.id.radio500 -> {
                    glassSize = 500
                    binding.tilCustomGlass.visibility = View.GONE
                }
                R.id.radioCustom -> {
                    binding.tilCustomGlass.visibility = View.VISIBLE
                }
            }
        }

        // Custom glass size input
        binding.etCustomGlass.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val customValue = binding.etCustomGlass.text.toString().toIntOrNull()
                if (customValue != null && customValue > 0) {
                    glassSize = customValue
                }
            }
        }

        // Get Started button
        binding.btnGetStarted.setOnClickListener {
            // Validate custom glass size if selected
            if (binding.radioCustom.isChecked) {
                val customValue = binding.etCustomGlass.text.toString().toIntOrNull()
                if (customValue == null || customValue <= 0) {
                    binding.tilCustomGlass.error = "Please enter a valid value"
                    return@setOnClickListener
                }
                glassSize = customValue
            }

            saveAndProceed()
        }
    }

    private fun saveAndProceed() {
        lifecycleScope.launch {
            val app = application as AquaReminderApp
            val profile = UserProfile(
                dailyGoal = dailyGoal,
                glassSize = glassSize,
                isPremium = false,
                darkModeEnabled = false
            )

            app.preferencesManager.saveUserProfile(profile)
            app.preferencesManager.setFirstTimeComplete()

            // Navigate to main activity
            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
