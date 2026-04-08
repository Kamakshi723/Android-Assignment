package com.example.q1_currencyconvert

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import android.content.Intent

class MainActivity : AppCompatActivity() {

    // UI elements
    private lateinit var etAmount: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var btnConvert: Button
    private lateinit var btnClear: Button
    private lateinit var btnSettings: Button
    private lateinit var tvResult: TextView
    private lateinit var scrollView: ScrollView

    // SharedPreferences to read dark mode setting
    private lateinit var sharedPreferences: SharedPreferences

    // Exchange rates
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "INR" to 83.50,
        "EUR" to 0.92,
        "GBP" to 0.79,
        "JPY" to 149.50,
        "AUD" to 1.53,
        "CAD" to 1.36,
        "AED" to 3.67,
        "SGD" to 1.34,
        "CHF" to 0.89
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect UI elements
        etAmount = findViewById(R.id.etAmount)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        btnConvert = findViewById(R.id.btnConvert)
        btnClear = findViewById(R.id.btnClear)
        btnSettings = findViewById(R.id.btnSettings)
        tvResult = findViewById(R.id.tvResult)
        scrollView = findViewById(R.id.scrollView)

        // Load SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        // Setup spinners
        setupSpinners()

        // Apply saved theme
        applyTheme()

        // Button listeners
        btnConvert.setOnClickListener {
            convertCurrency()
        }

        btnClear.setOnClickListener {
            clearFields()
        }
        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        // Reapply theme every time we come back from Settings
        applyTheme()
    }

    private fun setupSpinners() {
        val currencies = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            currencies
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter
        spinnerFrom.setSelection(currencies.indexOf("USD"))
        spinnerTo.setSelection(currencies.indexOf("INR"))
    }

    private fun convertCurrency() {
        val amountText = etAmount.text.toString()

        if (amountText.isEmpty()) {
            Toast.makeText(this, "Please enter an amount!", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDouble()
        val fromCurrency = spinnerFrom.selectedItem.toString()
        val toCurrency = spinnerTo.selectedItem.toString()
        val fromRate = exchangeRates[fromCurrency] ?: 1.0
        val toRate = exchangeRates[toCurrency] ?: 1.0
        val result = (amount / fromRate) * toRate
        val formatter = DecimalFormat("#,##0.00")
        val formattedResult = formatter.format(result)

        tvResult.text = "$formattedResult $toCurrency"

        Toast.makeText(
            this,
            "$amount $fromCurrency = $formattedResult $toCurrency",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun clearFields() {
        etAmount.text.clear()
        tvResult.text = "--"
        val currencies = exchangeRates.keys.toList()
        spinnerFrom.setSelection(currencies.indexOf("USD"))
        spinnerTo.setSelection(currencies.indexOf("INR"))
        Toast.makeText(this, "Cleared! 🗑️", Toast.LENGTH_SHORT).show()
    }

    private fun applyTheme() {
        val isDarkMode = sharedPreferences.getBoolean("darkMode", false)
        if (isDarkMode) {
            scrollView.setBackgroundColor(Color.parseColor("#121212"))
            tvResult.setTextColor(Color.parseColor("#90CAF9"))
        } else {
            scrollView.setBackgroundColor(Color.parseColor("#F0F4FF"))
            tvResult.setTextColor(Color.parseColor("#1A237E"))
        }
    }
}