package com.example.q3_sensorapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {

    // Sensor Manager — like a remote control for all sensors
    private lateinit var sensorManager: SensorManager

    // Individual sensors
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var magneticField: Sensor? = null

    // UI elements
    private lateinit var tvAccelerometer: TextView
    private lateinit var tvGyroscope: TextView
    private lateinit var tvMagneticField: TextView
    private lateinit var tvShake: TextView

    // Shake detection variables
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var lastShakeTime = 0L
    private val shakeThreshold = 800

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect UI elements
        tvAccelerometer = findViewById(R.id.tvAccelerometer)
        tvGyroscope = findViewById(R.id.tvGyroscope)
        tvMagneticField = findViewById(R.id.tvMagneticField)
        tvShake = findViewById(R.id.tvShake)

        // Initialize sensor manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Get each sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        // Check which sensors are available
        checkSensors()
    }

    private fun checkSensors() {
        if (accelerometer == null) {
            tvAccelerometer.text = "Accelerometer: Not available"
        }
        if (gyroscope == null) {
            tvGyroscope.text = "Gyroscope: Not available"
        }
        if (magneticField == null) {
            tvMagneticField.text = "Magnetic Field: Not available"
        }
    }

    override fun onResume() {
        super.onResume()
        // Start listening to sensors when app is visible
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyroscope?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        magneticField?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Stop listening when app goes to background
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        // Format values to 2 decimal places
        val x = String.format("%.2f", event.values[0])
        val y = String.format("%.2f", event.values[1])
        val z = String.format("%.2f", event.values[2])

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                tvAccelerometer.text = "X: $x  Y: $y  Z: $z"
                detectShake(event)
            }
            Sensor.TYPE_GYROSCOPE -> {
                tvGyroscope.text = "X: $x  Y: $y  Z: $z"
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                tvMagneticField.text = "X: $x  Y: $y  Z: $z"
            }
        }
    }

    private fun detectShake(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastShakeTime > 100) {
            val diffTime = currentTime - lastShakeTime
            lastShakeTime = currentTime

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val speed = sqrt(
                ((x - lastX) * (x - lastX) +
                        (y - lastY) * (y - lastY) +
                        (z - lastZ) * (z - lastZ)).toDouble()
            ) / diffTime * 10000

            if (speed > shakeThreshold) {
                tvShake.text = "📳 SHAKE DETECTED!"
            } else {
                tvShake.text = "Shake your phone!"
            }

            lastX = x
            lastY = y
            lastZ = z
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed but must be implemented
    }
}