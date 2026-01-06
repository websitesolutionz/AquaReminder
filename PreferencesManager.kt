package com.aquareminder.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.aquareminder.app.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "aqua_reminder_prefs")

class PreferencesManager(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        // User Profile Keys
        val DAILY_GOAL = intPreferencesKey("daily_goal")
        val GLASS_SIZE = intPreferencesKey("glass_size")
        val IS_PREMIUM = booleanPreferencesKey("is_premium")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val FIRST_TIME = booleanPreferencesKey("first_time")

        // Reminder Settings Keys
        val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
        val START_HOUR = intPreferencesKey("start_hour")
        val START_MINUTE = intPreferencesKey("start_minute")
        val END_HOUR = intPreferencesKey("end_hour")
        val END_MINUTE = intPreferencesKey("end_minute")
        val INTERVAL_MINUTES = intPreferencesKey("interval_minutes")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")

        // Today's Intake Keys
        val TODAY_DATE = stringPreferencesKey("today_date")
        val TODAY_INTAKE = intPreferencesKey("today_intake")

        // Statistics Keys
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val BEST_STREAK = intPreferencesKey("best_streak")
        val LAST_GOAL_REACHED_DATE = stringPreferencesKey("last_goal_reached_date")
        val HISTORY_JSON = stringPreferencesKey("history_json")
    }

    // Check if first time user
    suspend fun isFirstTime(): Boolean {
        return dataStore.data.first()[FIRST_TIME] ?: true
    }

    suspend fun setFirstTimeComplete() {
        dataStore.edit { prefs ->
            prefs[FIRST_TIME] = false
        }
    }

    // User Profile Methods
    suspend fun saveUserProfile(profile: UserProfile) {
        dataStore.edit { prefs ->
            prefs[DAILY_GOAL] = profile.dailyGoal
            prefs[GLASS_SIZE] = profile.glassSize
            prefs[IS_PREMIUM] = profile.isPremium
            prefs[DARK_MODE] = profile.darkModeEnabled
        }
    }

    fun getUserProfile(): Flow<UserProfile> = dataStore.data.map { prefs ->
        UserProfile(
            dailyGoal = prefs[DAILY_GOAL] ?: 2000,
            glassSize = prefs[GLASS_SIZE] ?: 250,
            isPremium = prefs[IS_PREMIUM] ?: false,
            darkModeEnabled = prefs[DARK_MODE] ?: false
        )
    }

    suspend fun updateDailyGoal(goal: Int) {
        dataStore.edit { prefs ->
            prefs[DAILY_GOAL] = goal
        }
    }

    suspend fun updateGlassSize(size: Int) {
        dataStore.edit { prefs ->
            prefs[GLASS_SIZE] = size
        }
    }

    suspend fun setPremium(isPremium: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_PREMIUM] = isPremium
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[DARK_MODE] = enabled
        }
    }

    // Reminder Settings Methods
    suspend fun saveReminderSettings(settings: ReminderSettings) {
        dataStore.edit { prefs ->
            prefs[REMINDERS_ENABLED] = settings.enabled
            prefs[START_HOUR] = settings.startHour
            prefs[START_MINUTE] = settings.startMinute
            prefs[END_HOUR] = settings.endHour
            prefs[END_MINUTE] = settings.endMinute
            prefs[INTERVAL_MINUTES] = settings.intervalMinutes
            prefs[SOUND_ENABLED] = settings.soundEnabled
            prefs[VIBRATION_ENABLED] = settings.vibrationEnabled
        }
    }

    fun getReminderSettings(): Flow<ReminderSettings> = dataStore.data.map { prefs ->
        ReminderSettings(
            enabled = prefs[REMINDERS_ENABLED] ?: true,
            startHour = prefs[START_HOUR] ?: 8,
            startMinute = prefs[START_MINUTE] ?: 0,
            endHour = prefs[END_HOUR] ?: 22,
            endMinute = prefs[END_MINUTE] ?: 0,
            intervalMinutes = prefs[INTERVAL_MINUTES] ?: 60,
            soundEnabled = prefs[SOUND_ENABLED] ?: true,
            vibrationEnabled = prefs[VIBRATION_ENABLED] ?: true
        )
    }

    // Today's Intake Methods
    suspend fun addWaterIntake(amount: Int) {
        val today = WaterIntake.getTodayDate()
        dataStore.edit { prefs ->
            val savedDate = prefs[TODAY_DATE] ?: ""
            
            // Check if it's a new day
            if (savedDate != today) {
                // Save yesterday's data to history before resetting
                val yesterdayIntake = prefs[TODAY_INTAKE] ?: 0
                if (savedDate.isNotEmpty() && yesterdayIntake > 0) {
                    val goal = prefs[DAILY_GOAL] ?: 2000
                    saveToHistory(prefs, savedDate, yesterdayIntake, goal)
                    updateStreak(prefs, savedDate, yesterdayIntake >= goal)
                }
                
                // Reset for new day
                prefs[TODAY_DATE] = today
                prefs[TODAY_INTAKE] = amount
            } else {
                // Add to today's intake
                val current = prefs[TODAY_INTAKE] ?: 0
                prefs[TODAY_INTAKE] = current + amount
            }
        }
    }

    suspend fun resetTodayIntake() {
        val today = WaterIntake.getTodayDate()
        dataStore.edit { prefs ->
            prefs[TODAY_DATE] = today
            prefs[TODAY_INTAKE] = 0
        }
    }

    fun getTodayIntake(): Flow<Int> = dataStore.data.map { prefs ->
        val today = WaterIntake.getTodayDate()
        val savedDate = prefs[TODAY_DATE] ?: ""
        
        if (savedDate == today) {
            prefs[TODAY_INTAKE] ?: 0
        } else {
            0
        }
    }

    fun getTodayRecord(): Flow<DailyRecord> = dataStore.data.map { prefs ->
        val today = WaterIntake.getTodayDate()
        val savedDate = prefs[TODAY_DATE] ?: ""
        val intake = if (savedDate == today) prefs[TODAY_INTAKE] ?: 0 else 0
        val goal = prefs[DAILY_GOAL] ?: 2000
        
        DailyRecord(
            date = today,
            totalAmount = intake,
            goal = goal,
            goalReached = intake >= goal
        )
    }

    // History and Statistics Methods
    private suspend fun saveToHistory(prefs: MutablePreferences, date: String, amount: Int, goal: Int) {
        val historyJson = prefs[HISTORY_JSON] ?: "[]"
        val historyArray = JSONArray(historyJson)
        
        // Create new record
        val record = JSONObject().apply {
            put("date", date)
            put("amount", amount)
            put("goal", goal)
            put("goalReached", amount >= goal)
        }
        
        // Add to beginning of array
        historyArray.put(0, record)
        
        // Keep only last 90 days for free users, unlimited for premium
        val isPremium = prefs[IS_PREMIUM] ?: false
        val maxRecords = if (isPremium) Int.MAX_VALUE else 90
        
        // Trim if necessary
        if (historyArray.length() > maxRecords) {
            val trimmedArray = JSONArray()
            for (i in 0 until maxRecords) {
                trimmedArray.put(historyArray.getJSONObject(i))
            }
            prefs[HISTORY_JSON] = trimmedArray.toString()
        } else {
            prefs[HISTORY_JSON] = historyArray.toString()
        }
    }

    private suspend fun updateStreak(prefs: MutablePreferences, date: String, goalReached: Boolean) {
        val currentStreak = prefs[CURRENT_STREAK] ?: 0
        val bestStreak = prefs[BEST_STREAK] ?: 0
        val lastGoalDate = prefs[LAST_GOAL_REACHED_DATE] ?: ""
        
        if (goalReached) {
            val yesterday = getYesterdayDate(date)
            val newStreak = if (lastGoalDate == yesterday || lastGoalDate.isEmpty()) {
                currentStreak + 1
            } else {
                1 // Reset streak if day was missed
            }
            
            prefs[CURRENT_STREAK] = newStreak
            prefs[LAST_GOAL_REACHED_DATE] = date
            
            if (newStreak > bestStreak) {
                prefs[BEST_STREAK] = newStreak
            }
        } else {
            prefs[CURRENT_STREAK] = 0
        }
    }

    fun getStatistics(): Flow<Statistics> = dataStore.data.map { prefs ->
        val currentStreak = prefs[CURRENT_STREAK] ?: 0
        val bestStreak = prefs[BEST_STREAK] ?: 0
        val historyJson = prefs[HISTORY_JSON] ?: "[]"
        val historyArray = JSONArray(historyJson)
        
        val last7Days = mutableListOf<DailyRecord>()
        var totalWater = 0L
        
        // Parse last 7 days
        for (i in 0 until minOf(7, historyArray.length())) {
            val record = historyArray.getJSONObject(i)
            val dailyRecord = DailyRecord(
                date = record.getString("date"),
                totalAmount = record.getInt("amount"),
                goal = record.getInt("goal"),
                goalReached = record.getBoolean("goalReached")
            )
            last7Days.add(dailyRecord)
            totalWater += dailyRecord.totalAmount
        }
        
        // Add today's record
        val today = WaterIntake.getTodayDate()
        val todayDate = prefs[TODAY_DATE] ?: ""
        val todayIntake = if (todayDate == today) prefs[TODAY_INTAKE] ?: 0 else 0
        val goal = prefs[DAILY_GOAL] ?: 2000
        
        if (last7Days.isEmpty() || last7Days[0].date != today) {
            last7Days.add(0, DailyRecord(today, todayIntake, goal, todayIntake >= goal))
            totalWater += todayIntake
            if (last7Days.size > 7) {
                totalWater -= last7Days.removeLast().totalAmount
            }
        }
        
        val averageDaily = if (last7Days.isNotEmpty()) {
            (totalWater / last7Days.size).toInt()
        } else {
            0
        }
        
        Statistics(
            currentStreak = currentStreak,
            bestStreak = bestStreak,
            totalWaterConsumed = totalWater,
            averageDailyIntake = averageDaily,
            last7Days = last7Days
        )
    }

    private fun getYesterdayDate(date: String): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.time = dateFormat.parse(date) ?: Date()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            dateFormat.format(calendar.time)
        } catch (e: Exception) {
            ""
        }
    }
}
