import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
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
fun WaveShape(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val waveHeight = 50.dp.toPx() // Altura de la ola
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(0f, height - waveHeight)
            quadraticBezierTo(
                width / 4, height + waveHeight,
                width / 2, height - waveHeight
            )
            quadraticBezierTo(
                3 * width / 4, height - 3 * waveHeight,
                width, height - waveHeight
            )
            lineTo(width, 0f)
            lineTo(0f, 0f)
            close()
        }

        drawIntoCanvas {
            drawPath(path, color)
        }
    }
}

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.hsl(100f, 0.3f, 0.6f)) // Fondo azul detrás de todo
    ) {
        // Nuevas figuras o cajas detrás de las olas en la parte superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.TopCenter)
                .background(color = Color.hsl(120f, 0.4f, 0.5f)) // Ejemplo de caja azul
        )

        // Fondo en forma de ola verde en la parte superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.TopCenter)
        ) {
            WaveShape(modifier = Modifier.fillMaxSize(), color = Color.hsl(100f, 0.4f, 0.5f))
        }

        // Nuevas figuras o cajas detrás de las olas en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
                .background(color = Color.hsl(100f, 0.4f, 0.5f)) // Ejemplo de caja azul
        )

        // Fondo en forma de ola verde en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
        ) {
            WaveShape(modifier = Modifier.fillMaxSize(), color = Color.hsl(120f, 0.4f, 0.5f))
        }

        // Contenido principal de la pantalla
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

            // Campo de texto para Email
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para Contraseña
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

            // Botón de inicio de sesión
            Button(
                onClick = {
                    loginMessage = null // Limpiar mensaje antes de intentar iniciar sesión
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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(120f, 0.5f, 0.5f)) // Button color set to green
            ) {
                Text("Entrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Texto de olvidó su contraseña
            TextButton(onClick = { /* TODO: Manejar olvidar contraseña */ }) {
                Text("¿Olvidaste tu Contraseña?", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de inicio de sesión
            if (loginMessage != null) {
                Text(
                    text = loginMessage!!,
                    color = if (loginMessage == "Congratulations, you logged in") Color.Green else Color.Red,
                    fontSize = 16.sp
                )
            }
        }
    }
}

suspend fun checkCredentials(email: String, password: String): Boolean {
    // Inicializar HTTP Client
    val client = OkHttpClient()

    // Hacer petición GET para obtener datos de la cuenta por email
    val request = Request.Builder()
        .url("https://api-generator.retool.com/HCzRU7/accounts?email=$email")
        .build()

    return try {
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        // Log para depuración
        println("API Response: $responseBody")

        // Validar que el cuerpo de la respuesta no sea nulo o vacío
        if (responseBody.isNullOrEmpty()) {
            println("No data returned from API.")
            return false
        }

        val jsonArray = JSONArray(responseBody)

        // Verificar si se encontró un usuario
        if (jsonArray.length() > 0) {
            val account = jsonArray.getJSONObject(0) // Asumir solo un usuario por email
            val storedPassword = account.getString("password")

            println("Stored password: $storedPassword, Entered password: $password")

            // Validar la contraseña
            if (password == storedPassword) {
                println("Login successful!")
                return true // Inicio de sesión exitoso
            } else {
                println("Password mismatch.")
            }
        } else {
            println("No user found with the given email.")
        }

        false // Inicio de sesión fallido si no hay usuario o contraseña incorrecta
    } catch (e: Exception) {
        e.printStackTrace()
        false // Manejar error de red o parsing
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
