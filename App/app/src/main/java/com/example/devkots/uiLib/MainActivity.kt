package com.example.devkots.uiLib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.devkots.uiLib.theme.DevKotsTheme
import com.example.devkots.uiLib.screens.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevKotsTheme { // Apply DevKotsTheme here
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}