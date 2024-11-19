package com.example.devkots.uiLib.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.uiLib.components.BackButton
import com.example.devkots.uiLib.theme.*
import com.example.devkots.uiLib.viewmodels.LoginViewModel
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.devkots.R

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    userSessionViewModel: UserSessionViewModel
) {
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val loginSuccess by loginViewModel.loginSuccess.collectAsState()
    val errorMessage by loginViewModel.errorMessage.collectAsState()

    // Navigate to DashboardScreen after login
    if (loginSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate("dashboard") {
                popUpTo("login_signup") { inclusive = true }
            }
        }
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = White
        )
        {
            Box(modifier = Modifier.fillMaxSize()
                .fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.vector_1),
                    contentDescription = "vector_3",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(380.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.vector_3),
                    contentDescription = "vector_3",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.204f)
                        .align(Alignment.BottomCenter)
                )
                Image(
                    painter = painterResource(id = R.drawable.vector_5),
                    contentDescription = "vector_5",
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .align(Alignment.TopStart)
                        .width(200.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.vector_6),
                    contentDescription = "vector_6",
                    modifier = Modifier
                        .fillMaxWidth()

                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Back Button
                    Box(modifier = Modifier.padding(end = 500.dp)) {
                        BackButton(navController)
                    }

                    Spacer(modifier = Modifier.height(50.dp))
                    // Welcome Text
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = "Bienvenido",
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                        Spacer(modifier = Modifier.height(200.dp))
                        Text(
                            text = "Inicia Sesión",
                            fontSize = 40.sp,
                            color = Black,
                            fontWeight = FontWeight.Bold
                        )
                    }


                    // Email and Password Inputs
                    OutlinedTextField(
                        value = email,
                        onValueChange = { loginViewModel.onEmailChange(it) },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { loginViewModel.onPasswordChange(it) },
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
                    Spacer(modifier = Modifier.height(50.dp))
                    // Login Button
                    Button(
                        onClick = { loginViewModel.login() },
                        modifier = Modifier
                            .height(56.dp)
                            .width(200.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen1)
                    ) {
                        Text("Entrar", color = Color.White, fontSize = 30.sp)
                    }
                }
            }
        }
    }
}
