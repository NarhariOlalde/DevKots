package com.example.devkots.UILib

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido\nInicia Sesión",
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                loginMessage = null // Clear message before new login attempt
                CoroutineScope(Dispatchers.IO).launch {
                    val success = checkCredentials(email, password)
                    withContext(Dispatchers.Main) {
                        loginMessage = if (success) {
                            "Congratulations, you logged in"
                        } else {
                            "Wrong user or password"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password Text
        TextButton(onClick = { /* TODO: Handle forgot password */ }) {
            Text("¿Olvidaste tu Contraseña?", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Message
        if (loginMessage != null) {
            Text(
                text = loginMessage!!,
                color = if (loginMessage == "Congratulations, you logged in") Color.Green else Color.Red,
                fontSize = 16.sp
            )
        }
    }
}

suspend fun checkCredentials(email: String, password: String): Boolean {
    // Initialize HTTP Client
    val client = OkHttpClient()

    // Make GET request to fetch account data by email
    val request = Request.Builder()
        .url("https://api-generator.retool.com/HCzRU7/accounts?email=$email")
        .build()

    return try {
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        // Log the raw response for debugging
        println("API Response: $responseBody")

        // Ensure the response body is not null or empty
        if (responseBody.isNullOrEmpty()) {
            println("No data returned from API.")
            return false
        }

        val jsonArray = JSONArray(responseBody)

        // Check if we got a user record
        if (jsonArray.length() > 0) {
            val account = jsonArray.getJSONObject(0) // Assume only one user per email
            val storedPassword = account.getString("password")

            println("Stored password: $storedPassword, Entered password: $password")

            // Validate password (convert both to string to avoid type mismatch)
            if (password == storedPassword) {
                println("Login successful!")
                return true // Login successful
            } else {
                println("Password mismatch.")
            }
        } else {
            println("No user found with the given email.")
        }

        false // Login failed if no user or wrong password
    } catch (e: Exception) {
        e.printStackTrace()
        false // Handle network or parsing error
    }
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}