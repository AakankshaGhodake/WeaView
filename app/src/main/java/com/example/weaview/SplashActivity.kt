package com.example.weaview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.content.Intent
import android.os.Handler
//import kotlinx.android.synthetic.main.activity_main.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
           val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}