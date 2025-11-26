package com.example.app3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Install the splash screen
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // 2. Keep the splash screen visible while the app decides the next activity (optional)
        // In this case, we go directly to MainActivity, so no delay is needed.

        // 3. Start the main activity
        startActivity(Intent(this, MainActivity::class.java))
        
        // 4. Finish this activity so the user can't navigate back to it
        finish()
    }
}
