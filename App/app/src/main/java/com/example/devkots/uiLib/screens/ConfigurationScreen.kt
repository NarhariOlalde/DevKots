package com.example.devkots.uiLib.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.components.TopNavigationBar
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.theme.ObjectGreen4
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel
import com.example.devkots.util.HashUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ConfigurationScreen(
    navController: NavController,
    userSessionViewModel: UserSessionViewModel
) {
    // Collect user information from UserSessionViewModel
    val nameState = userSessionViewModel.userName.collectAsState()
    val emailState = userSessionViewModel.email.collectAsState()
    var password by remember { mutableStateOf("") }

    // Local variables for name and email based on collected states
    var name by remember { mutableStateOf(nameState.value) }
    var email by remember { mutableStateOf(emailState.value) }
    var imageBase64 : String? by remember { mutableStateOf(userSessionViewModel.imageBase64.value) }


    MainLayout(navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = { Text("Configuración", fontSize = 24.sp, modifier = Modifier.padding(start = 40.dp))},
                backgroundColor = Color(0xFFB4D68F),
                modifier = Modifier.height(80.dp),
            )

            // Logout Button
            Button(
                onClick = {
                    userSessionViewModel.logoutUser()
                    navController.navigate("login_signup") {
                        popUpTo("login_signup") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Cerrar Sesión", color = Color.White, fontSize = 30.sp)
            }
        }
    }
}