package com.aquareminder.app.ui.fragments

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aquareminder.app.AquaReminderApp
import com.aquareminder.app.R
import com.aquareminder.app.databinding.FragmentRemindersBinding
import com.aquareminder.app.model.ReminderSettings
import com.aquareminder.app.notification.NotificationHelper
import kotlinx.coroutines.launch

class RemindersFragment : Fragment() {

    private var _binding: FragmentRemindersBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var prefsManager: com.aquareminder.app.data.PreferencesManager
    private lateinit var notificationHelper: NotificationHelper
    
    private var currentSettings = ReminderSettings()

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                requireContext(),
                "Notification permission required for reminders",
                Toast.LENGTH_LONG
            ).show()
            binding.switchReminders.isChecked = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRemindersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val app = requireActivity().application as AquaReminderApp
        prefsManager = app.preferencesManager
        notificationHelper = NotificationHelper(requireContext())
        
        setupObservers()
        setupListeners()
        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.getReminderSettings().collect { settings ->
                currentSettings = settings
                updateUI(settings)
            }
        }
    }

    private fun updateUI(settings: ReminderSettings) {
        binding.switchReminders.isChecked = settings.enabled
        binding.tvStartTime.text = settings.getStartTimeString()
        binding.tvEndTime.text = settings.getEndTimeString()
        binding.switchSound.isChecked = settings.soundEnabled
        binding.switchVibration.isChecked = settings.vibrationEnabled
        
        when (settings.intervalMinutes) {
            30 -> binding.radio30.isChecked = true
            60 -> binding.radio60.isChecked = true
            90 -> binding.radio90.isChecked = true
        }
        
        // Enable/disable time settings based on reminder switch
        binding.cardTimeSettings.alpha = if (settings.enabled) 1.0f else 0.5f
    }

    private fun setupListeners() {
        binding.switchReminders.setOnCheckedChangeListener { _, isChecked ->
            binding.cardTimeSettings.alpha = if (isChecked) 1.0f else 0.5f
        }

        binding.tvStartTime.setOnClickListener {
            if (binding.switchReminders.isChecked) {
                showTimePicker(true)
            }
        }

        binding.tvEndTime.setOnClickListener {
            if (binding.switchReminders.isChecked) {
                showTimePicker(false)
            }
        }

        binding.btnSave.setOnClickListener {
            saveSettings()
        }
    }

    private fun showTimePicker(isStartTime: Boolean) {
        val hour = if (isStartTime) currentSettings.startHour else currentSettings.endHour
        val minute = if (isStartTime) currentSettings.startMinute else currentSettings.endMinute
        
        TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val timeString = String.format("%02d:%02d", selectedHour, selectedMinute)
                if (isStartTime) {
                    binding.tvStartTime.text = timeString
                } else {
                    binding.tvEndTime.text = timeString
                }
            },
            hour,
            minute,
            true
        ).show()
    }

    private fun saveSettings() {
        viewLifecycleOwner.lifecycleScope.launch {
            val intervalMinutes = when {
                binding.radio30.isChecked -> 30
                binding.radio60.isChecked -> 60
                binding.radio90.isChecked -> 90
                else -> 60
            }
            
            // Parse time strings
            val startTimeParts = binding.tvStartTime.text.toString().split(":")
            val endTimeParts = binding.tvEndTime.text.toString().split(":")
            
            val newSettings = ReminderSettings(
                enabled = binding.switchReminders.isChecked,
                startHour = startTimeParts[0].toInt(),
                startMinute = startTimeParts[1].toInt(),
                endHour = endTimeParts[0].toInt(),
                endMinute = endTimeParts[1].toInt(),
                intervalMinutes = intervalMinutes,
                soundEnabled = binding.switchSound.isChecked,
                vibrationEnabled = binding.switchVibration.isChecked
            )
            
            prefsManager.saveReminderSettings(newSettings)
            
            // Schedule or cancel reminders
            notificationHelper.scheduleReminders(newSettings)
            
            Toast.makeText(requireContext(), "Settings saved!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
