package com.aquareminder.app.model

import java.text.SimpleDateFormat
import java.util.*

data class WaterIntake(
    val date: String,
    val amount: Int, // in ml
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        fun getTodayDate(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(Date())
        }
    }
}

data class DailyRecord(
    val date: String,
    val totalAmount: Int, // in ml
    val goal: Int, // in ml
    val goalReached: Boolean
) {
    fun getProgress(): Float {
        return (totalAmount.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    }
    
    fun getProgressPercentage(): Int {
        return (getProgress() * 100).toInt()
    }
}

data class Statistics(
    val currentStreak: Int,
    val bestStreak: Int,
    val totalWaterConsumed: Long, // in ml
    val averageDailyIntake: Int, // in ml
    val last7Days: List<DailyRecord>
)

data class ReminderSettings(
    val enabled: Boolean = true,
    val startHour: Int = 8,
    val startMinute: Int = 0,
    val endHour: Int = 22,
    val endMinute: Int = 0,
    val intervalMinutes: Int = 60,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true
) {
    fun getStartTimeInMinutes(): Int = startHour * 60 + startMinute
    fun getEndTimeInMinutes(): Int = endHour * 60 + endMinute
    
    fun getStartTimeString(): String {
        return String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute)
    }
    
    fun getEndTimeString(): String {
        return String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute)
    }
}

data class UserProfile(
    val dailyGoal: Int = 2000, // in ml
    val glassSize: Int = 250, // in ml
    val isPremium: Boolean = false,
    val darkModeEnabled: Boolean = false
)
