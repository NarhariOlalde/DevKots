package com.example.devkots.uiLib.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel
import com.example.devkots.util.HashUtil

@Composable
fun ConfigurationScreen(
    navController: NavController,
    userSessionViewModel: UserSessionViewModel
) {
    // Collect user information from UserSessionViewModel
    val nameState = userSessionViewModel.userName.collectAsState()
    val emailState = userSessionViewModel.email.collectAsState()
    var password by remember { mutableStateOf("") }

    // Track if the update was successful
    val updateStatus by userSessionViewModel.updateStatus.collectAsState()

    // Local variables for name and email based on collected states
    var name by remember { mutableStateOf(nameState.value) }
    var email by remember { mutableStateOf(emailState.value) }

    // Enable save button only when all fields are non-blank
    val isSaveEnabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank()

    MainLayout(navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Configuración", fontSize = 24.sp, color = ObjectGreen1)

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    val hashedPassword = HashUtil.sha256(password)
                    userSessionViewModel.updateUserInfo(name, email, hashedPassword)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isSaveEnabled,
                colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen2)
            ) {
                Text("Save Changes", color = Color.White)
            }

            // Display Update Status
            updateStatus?.let { success ->
                Text(
                    text = if (success) "Update successful" else "Update failed",
                    color = if (success) Color.Green else Color.Red
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = {
                    userSessionViewModel.logoutUser()
                    navController.navigate("login_signup") {
                        popUpTo("login_signup") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Logout", color = Color.White)
            }
        }
    }
}