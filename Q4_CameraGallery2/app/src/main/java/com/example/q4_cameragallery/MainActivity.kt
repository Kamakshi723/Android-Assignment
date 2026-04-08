package com.example.q4_cameragallery

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnCamera: Button
    private lateinit var btnGallery: Button
    private lateinit var tvStatus: TextView

    private var photoUri: Uri? = null

    // Camera launcher — opens camera and waits for result
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageView.setImageURI(photoUri)
            tvStatus.text = "📸 Photo taken successfully!"
        } else {
            tvStatus.text = "Camera cancelled"
        }
    }

    // Gallery launcher — opens gallery and waits for result
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imageView.setImageURI(uri)
            tvStatus.text = "🖼️ Image selected from gallery!"
        } else {
            tvStatus.text = "Gallery cancelled"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect UI elements
        imageView = findViewById(R.id.imageView)
        btnCamera = findViewById(R.id.btnCamera)
        btnGallery = findViewById(R.id.btnGallery)
        tvStatus = findViewById(R.id.tvStatus)

        // Camera button click
        btnCamera.setOnClickListener {
            openCamera()
        }

        // Gallery button click
        btnGallery.setOnClickListener {
            openGallery()
        }
    }

    private fun openCamera() {
        // Check camera permission first
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted — ask for it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
            return
        }

        // Create a file to save the photo
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "com.example.q4_cameragallery.fileprovider",
            photoFile
        )

        // Open the camera
        photoUri?.let { uri ->
            cameraLauncher.launch(uri)
        }
    }

    private fun openGallery() {
        // Open gallery — no permission needed for modern Android
        galleryLauncher.launch("image/*")
    }

    private fun createImageFile(): File {
        // Create a unique filename using current timestamp
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "PHOTO_${timestamp}"

        // Get the directory to save the photo
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Create and return the file
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted — open camera
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}