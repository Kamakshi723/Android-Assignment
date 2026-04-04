package com.example.q1_currencyconvert

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var etAmount: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var btnConvert: Button
    private lateinit var tvResult: TextView

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
        etAmount = findViewById(R.id.etAmount)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        btnConvert = findViewById(R.id.btnConvert)
        tvResult = findViewById(R.id.tvResult)

        setupSpinners()
        btnConvert.setOnClickListener {
            convertCurrency()
        }

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

        Toast.makeText(this, "$amount $fromCurrency = $formattedResult $toCurrency", Toast.LENGTH_LONG).show()
    }
    }