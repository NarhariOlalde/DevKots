package com.example.devkots

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.devkots.uiLib.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Fade-out animation
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 1000
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Transition to MainActivity after the animation ends
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Start the fade-out animation after a delay (splash duration)
        Handler(Looper.getMainLooper()).postDelayed({
            findViewById<ImageView>(R.id.splash_image).startAnimation(fadeOut)
        }, 2000)
    }
}