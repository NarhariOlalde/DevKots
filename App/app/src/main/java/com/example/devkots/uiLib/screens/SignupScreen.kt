package com.example.devkots.uiLib.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.uiLib.components.BackButton
import com.example.devkots.uiLib.theme.Black
import com.example.devkots.uiLib.theme.IntroGreen
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.theme.White
import com.example.devkots.uiLib.viewmodels.SignupViewModel
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel

@Composable
fun SignupScreen(
    navController: NavController,
    signupViewModel: SignupViewModel,
    userSessionViewModel: UserSessionViewModel
) {
    val name by signupViewModel.name.collectAsState()
    val email by signupViewModel.email.collectAsState()
    val password by signupViewModel.password.collectAsState()
    val signupSuccess by signupViewModel.signupSuccess.collectAsState()
    val errorMessage by signupViewModel.errorMessage.collectAsState()

    if (signupSuccess) {
        // Navigate to DashboardScreen after successful signup
        LaunchedEffect(Unit) {
            navController.navigate("dashboard") {
                popUpTo("login_signup") { inclusive = true }
            }
        }
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = White
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.vector_3),
                    contentDescription = "vector_3",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.175f)
                        .align(Alignment.TopCenter)
                        .graphicsLayer(rotationZ = 180f)
                )
                Image(
                    painter = painterResource(id = R.drawable.vector_3),
                    contentDescription = "vector_3",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.175f)
                        .align(Alignment.BottomCenter)
                )
                Image(
                    painter = painterResource(id = R.drawable.vector_5),
                    contentDescription = "vector_5",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.24f)
                        .align(Alignment.TopStart)
                        .width(200.dp)
                        .clip(RectangleShape),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.vector_6),
                    contentDescription = "vector_6",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .align(Alignment.TopCenter)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(13.dp)
                    ) {
                        // Back Button
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.TopStart
                        ) {
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
                                color = Black
                            )
                            Text(
                                text = "Regístrate",
                                fontSize = 24.sp,
                                color = White
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
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen1)
                        ) {
                            Text("Register", color = Color.White, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}