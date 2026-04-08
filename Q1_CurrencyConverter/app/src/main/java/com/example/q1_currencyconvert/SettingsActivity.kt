package com.example.q1_currencyconvert

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    // UI elements
    private lateinit var btnBack: Button
    private lateinit var btnLightMode: Button
    private lateinit var btnDarkMode: Button
    private lateinit var btnReset: Button
    private lateinit var tvCurrentTheme: TextView
    private lateinit var settingsScrollView: ScrollView

    // SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Connect UI elements
        btnBack = findViewById(R.id.btnBack)
        btnLightMode = findViewById(R.id.btnLightMode)
        btnDarkMode = findViewById(R.id.btnDarkMode)
        btnReset = findViewById(R.id.btnReset)
        tvCurrentTheme = findViewById(R.id.tvCurrentTheme)
        settingsScrollView = findViewById(R.id.settingsScrollView)

        // Load SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        // Apply current theme
        applyTheme()

        // Update theme status text
        updateThemeStatus()

        // Back button — go back to MainActivity
        btnBack.setOnClickListener {
            finish()
        }

        // Light mode button
        btnLightMode.setOnClickListener {
            setLightMode()
        }

        // Dark mode button
        btnDarkMode.setOnClickListener {
            setDarkMode()
        }

        // Reset button
        btnReset.setOnClickListener {
            resetSettings()
        }
    }

    private fun setLightMode() {
        // Save light mode preference
        sharedPreferences.edit().putBoolean("darkMode", false).apply()

        // Apply light theme
        applyTheme()

        // Update status
        updateThemeStatus()

        Toast.makeText(this, "Light Mode ON ☀️", Toast.LENGTH_SHORT).show()
    }

    private fun setDarkMode() {
        // Save dark mode preference
        sharedPreferences.edit().putBoolean("darkMode", true).apply()

        // Apply dark theme
        applyTheme()

        // Update status
        updateThemeStatus()

        Toast.makeText(this, "Dark Mode ON 🌙", Toast.LENGTH_SHORT).show()
    }

    private fun resetSettings() {
        // Reset all settings to default
        sharedPreferences.edit().clear().apply()

        // Apply default theme
        applyTheme()

        // Update status
        updateThemeStatus()

        Toast.makeText(this, "Settings Reset! 🔄", Toast.LENGTH_SHORT).show()
    }

    private fun updateThemeStatus() {
        val isDarkMode = sharedPreferences.getBoolean("darkMode", false)
        tvCurrentTheme.text = if (isDarkMode) {
            "Current Theme: Dark Mode 🌙"
        } else {
            "Current Theme: Light Mode ☀️"
        }
    }

    private fun applyTheme() {
        val isDarkMode = sharedPreferences.getBoolean("darkMode", false)
        if (isDarkMode) {
            settingsScrollView.setBackgroundColor(Color.parseColor("#121212"))
            tvCurrentTheme.setTextColor(Color.parseColor("#90CAF9"))
        } else {
            settingsScrollView.setBackgroundColor(Color.parseColor("#F0F4FF"))
            tvCurrentTheme.setTextColor(Color.parseColor("#666666"))
        }
    }
}