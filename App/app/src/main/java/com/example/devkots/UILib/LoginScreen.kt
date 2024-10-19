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
                    Icon(imageVector = image, contentDescription = "Cambiar Visibilidad de la Contraseña")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                loginMessage = null
                CoroutineScope(Dispatchers.IO).launch {
                    val success = checkCredentials(email, password)
                    withContext(Dispatchers.Main) {
                        loginMessage = if (success) {
                            "Haz hecho un login exitoso"
                        } else {
                            "Usuario o contraseña incorrecto"
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

        if (loginMessage != null) {
            Text(
                text = loginMessage!!,
                color = if (loginMessage == "Haz hecho un login exitoso") Color.Green else Color.Red,
                fontSize = 32.sp
            )
        }
    }
}

suspend fun checkCredentials(email: String, password: String): Boolean {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://api-generator.retool.com/HCzRU7/accounts?email=$email")
        .build()

    return try {
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        println("API Response: $responseBody")

        if (responseBody.isNullOrEmpty()) {
            println("No data returned from API.")
            return false
        }

        val jsonArray = JSONArray(responseBody)

        if (jsonArray.length() > 0) {
            val account = jsonArray.getJSONObject(0) // Assume only one user per email
            val storedPassword = account.getString("password")

            println("Stored password: $storedPassword, Entered password: $password")

            if (password == storedPassword) {
                println("Login successful!")
                return true // Login successful
            } else {
                println("Password mismatch.")
            }
        } else {
            println("No user found with the given email.")
        }

        false
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}