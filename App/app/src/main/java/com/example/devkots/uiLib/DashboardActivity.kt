package com.example.devkots.uiLib

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.devkots.R

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Retrieve the username from the Intent
        val username = intent.getStringExtra("USERNAME") ?: "User"
        findViewById<TextView>(R.id.user_greeting).text = "Hola, $username"
    }
}