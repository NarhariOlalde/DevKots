package com.example.devkots.uiLib.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.uiLib.components.BackButton
import com.example.devkots.uiLib.theme.IntroGreen
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.viewmodels.SignupViewModel
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel

@Composable
fun SignupScreen(
    navController: NavController,
    signupViewModel: SignupViewModel,
) {
    val name by signupViewModel.name.collectAsState()
    val email by signupViewModel.email.collectAsState()
    val password by signupViewModel.password.collectAsState()
    val signupSuccess by signupViewModel.signupSuccess.collectAsState()
    val errorMessage by signupViewModel.errorMessage.collectAsState()

    if (signupSuccess) {
        navController.navigate("dashboard") { // Replace "main" with your actual destination for logged-in users
            popUpTo("signup") { inclusive = true }
        }
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = IntroGreen
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Back Button
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                    BackButton(navController)
                }

                // Title Section
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Crea una cuenta",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = ObjectGreen1
                    )
                    Text(
                        text = "Regístrate",
                        fontSize = 24.sp,
                        color = ObjectGreen1
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { signupViewModel.onNameChange(it) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Email Input
                OutlinedTextField(
                    value = email,
                    onValueChange = { signupViewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Password Input
                OutlinedTextField(
                    value = password,
                    onValueChange = { signupViewModel.onPasswordChange(it) },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                // Error Message Display
                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Register Button
                Button(
                    onClick = { signupViewModel.register() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen2)
                ) {
                    Text("Register", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}