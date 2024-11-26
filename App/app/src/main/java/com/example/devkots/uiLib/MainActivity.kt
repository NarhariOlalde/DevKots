package com.example.devkots.uiLib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.auth0.android.Auth0
import com.example.devkots.uiLib.theme.DevKotsTheme
import com.example.devkots.uiLib.screens.AppNavigation

class MainActivity : ComponentActivity() {
    private lateinit var account: Auth0
    override fun onCreate(savedInstanceState: Bundle?) {
        account = Auth0.getInstance(
            "sxlOUkpm591uWTxtwpKguSg0rFneEFjJ",
            "dev-6vzyeoluir57rec7.us.auth0.com"
        )
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            DevKotsTheme { // Apply DevKotsTheme here
                val navController = rememberNavController()
                AppNavigation(navController, auth0 = account)
            }
        }
    }
}