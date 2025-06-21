package com.example.mediauploader

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mediauploader.R.id.fullVideoView

class FullScreenMediaActivity : AppCompatActivity() {
    private val serverUrl = "Your Flask IP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_media)

        val fileName = intent.getStringExtra("fileName") ?: return
        val mediaUrl = "$serverUrl/media/$fileName"

        val imageView = findViewById<ImageView>(R.id.fullImageView)
        val videoView = findViewById<VideoView>(fullVideoView)

        if (fileName.endsWith(".mp4") || fileName.endsWith(".avi")) {
            videoView.setVideoURI(Uri.parse(mediaUrl))
            videoView.setOnPreparedListener { it.start() }
            videoView.visibility = VideoView.VISIBLE
            imageView.visibility = ImageView.GONE
        } else {
            Glide.with(this).load(mediaUrl).into(imageView)
            imageView.visibility = ImageView.VISIBLE
            videoView.visibility = VideoView.GONE
        }
    }
}
