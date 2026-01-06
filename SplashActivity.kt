package com.aquareminder.app.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aquareminder.app.AquaReminderApp
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show splash for 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            checkFirstTime()
        }, 2000)
    }

    private fun checkFirstTime() {
        lifecycleScope.launch {
            val app = application as AquaReminderApp
            val isFirstTime = app.preferencesManager.isFirstTime()

            val intent = if (isFirstTime) {
                Intent(this@SplashActivity, WelcomeActivity::class.java)
            } else {
                Intent(this@SplashActivity, MainActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}
