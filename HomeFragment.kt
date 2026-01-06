package com.aquareminder.app.ui.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aquareminder.app.AquaReminderApp
import com.aquareminder.app.R
import com.aquareminder.app.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var prefsManager: com.aquareminder.app.data.PreferencesManager
    private var currentIntake = 0
    private var dailyGoal = 2000
    private var glassSize = 250
    private var isPremium = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
            // Observe user profile
            prefsManager.getUserProfile().collect { profile ->
                dailyGoal = profile.dailyGoal
                glassSize = profile.glassSize
                isPremium = profile.isPremium
                
                binding.tvGoal.text = "/ $dailyGoal ml"
                
                // Show/hide ads based on premium status
                binding.adContainer.visibility = if (isPremium) View.GONE else View.VISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // Observe today's intake
            prefsManager.getTodayRecord().collect { record ->
                currentIntake = record.totalAmount
                updateUI(record.totalAmount, record.goal)
                
                if (record.goalReached && record.totalAmount <= record.goal + glassSize) {
                    // Just reached goal
                    showGoalReachedDialog()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnDrinkWater.setOnClickListener {
            addWaterIntake()
        }

        binding.ivReset.setOnClickListener {
            showResetDialog()
        }
    }

    private fun addWaterIntake() {
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.addWaterIntake(glassSize)
            
            // Animate button
            animateButton()
        }
    }

    private fun updateUI(intake: Int, goal: Int) {
        binding.tvCurrentIntake.text = intake.toString()
        
        val percentage = ((intake.toFloat() / goal.toFloat()) * 100).toInt().coerceIn(0, 100)
        binding.tvPercentage.text = "$percentage%"
        
        // Animate progress
        animateProgress(percentage)
        
        // Update motivation text
        binding.tvMotivation.text = when {
            percentage >= 100 -> getString(R.string.goal_reached)
            percentage >= 75 -> "Almost there! Keep going!"
            percentage >= 50 -> "You're halfway there!"
            percentage >= 25 -> "Great start! Keep it up!"
            else -> getString(R.string.keep_going)
        }
    }

    private fun animateProgress(targetPercentage: Int) {
        val animator = ObjectAnimator.ofInt(
            binding.circularProgress,
            "progress",
            binding.circularProgress.progress,
            targetPercentage
        )
        animator.duration = 800
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    private fun animateButton() {
        binding.btnDrinkWater.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                binding.btnDrinkWater.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun showGoalReachedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("🎉 Congratulations!")
            .setMessage("You've reached your daily water goal! Keep up the great work!")
            .setPositiveButton("Awesome!") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showResetDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Reset Progress")
            .setMessage(getString(R.string.confirm_reset))
            .setPositiveButton("Reset") { _, _ ->
                resetProgress()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun resetProgress() {
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.resetTodayIntake()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
