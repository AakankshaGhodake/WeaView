package com.example.weaview

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView

//var mapView: MapView? = null
class LocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        Handler(Looper.getMainLooper()).postDelayed({
            val url = "https://www.google.com/mymaps/viewer?mid=1tIbwg9n8tO-uXc_dKl8X9iaIyAA&hl=en_US"  // Replace with the desired URL
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
            finish() // Finish this activity to prevent going back to it
        }, 5000)
    }
}