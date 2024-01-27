package com.example.unishop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

// Убедитесь, что R импортирован, если он находится в другом пакете

class SplashActivity : AppCompatActivity() {
    private val SPLASH_SCREEN_DURATION = 1000 // Длительность отображения титульной страницы в миллисекундах

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen) // Убедитесь, что у вас есть ресурс layout с именем splash_screen.xml
        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_DURATION.toLong())
    }
}