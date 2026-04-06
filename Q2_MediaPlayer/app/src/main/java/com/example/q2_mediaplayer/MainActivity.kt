package com.example.q2_mediaplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Audio variables
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var btnAudioPlay: Button
    private lateinit var btnAudioPause: Button
    private lateinit var btnAudioStop: Button
    private lateinit var tvAudioStatus: TextView

    // Video variables
    private lateinit var videoView: VideoView
    private lateinit var btnVideoPlay: Button
    private lateinit var btnVideoPause: Button
    private lateinit var btnVideoStop: Button
    private lateinit var tvVideoStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect audio views
        btnAudioPlay = findViewById(R.id.btnAudioPlay)
        btnAudioPause = findViewById(R.id.btnAudioPause)
        btnAudioStop = findViewById(R.id.btnAudioStop)
        tvAudioStatus = findViewById(R.id.tvAudioStatus)

        // Connect video views
        videoView = findViewById(R.id.videoView)
        btnVideoPlay = findViewById(R.id.btnVideoPlay)
        btnVideoPause = findViewById(R.id.btnVideoPause)
        btnVideoStop = findViewById(R.id.btnVideoStop)
        tvVideoStatus = findViewById(R.id.tvVideoStatus)

        // Setup audio and video
        setupAudio()
        setupVideo()
    }

    private fun setupAudio() {
        btnAudioPlay.setOnClickListener {
            mediaPlayer?.setVolume(1.0f, 1.0f)
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.audio)
                }
                mediaPlayer?.start()
                tvAudioStatus.text = "Status: Playing 🎵"
            } catch (e: Exception) {
                tvAudioStatus.text = "Error: ${e.message}"
            }
        }

        btnAudioPause.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                tvAudioStatus.text = "Status: Paused ⏸"
            } else {
                tvAudioStatus.text = "Status: Nothing Playing"
            }
        }

        btnAudioStop.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            tvAudioStatus.text = "Status: Stopped ⏹"
        }
    }

    private fun setupVideo() {
        val videoUri = Uri.parse(
            "android.resource://${packageName}/${R.raw.video}"
        )
        videoView.setVideoURI(videoUri)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        btnVideoPlay.setOnClickListener {
            videoView.start()
            tvVideoStatus.text = "Status: Playing 🎬"
        }

        btnVideoPause.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()
                tvVideoStatus.text = "Status: Paused ⏸"
            }
        }

        btnVideoStop.setOnClickListener {
            videoView.stopPlayback()
            videoView.setVideoURI(videoUri)
            tvVideoStatus.text = "Status: Stopped ⏹"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}