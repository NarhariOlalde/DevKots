package com.example.devkots.uiLib.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devkots.R // Asegúrate de que el paquete sea correcto para tu archivo R
import com.example.devkots.uiLib.theme.ObjectGreen1 // Import the color from your theme

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Green rectangle in the background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.TopCenter)
                .background(ObjectGreen1) // Use ObjectGreen1 for background
        )

        // Back button at the top left corner, in front of the green rectangle
        IconButton(
            onClick = { /* Define back action */ },
            modifier = Modifier
                .padding(8.dp)
                .size(48.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(ObjectGreen1) // Background color of the button
        ) {
            Text(
                text = "<-",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White // Set the color of the symbol to white
            )
        }

        // Container to elevate both the circle and the text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp), // Elevate both elements by adjusting this padding
            contentAlignment = Alignment.Center
        ) {
            // Row with circle on the left and texts on the right
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // White circle on the left
                Box(
                    modifier = Modifier
                        .size(120.dp) // Size of the circle
                        .clip(CircleShape)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.width(16.dp)) // Space between the circle and the text

                // Column with main text and smaller text below it
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    // Main text
                    Text(
                        text = "Profile User",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Smaller text below
                    Text(
                        text = "Active since: ",
                        fontSize = 18.sp,
                        color = Color.Black // Color for the smaller text
                    )
                }
            }
        }

        // Email and Password input fields with placeholder image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 160.dp) // Position the input fields below the green rectangle
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Space between the fields
        ) {
            // States for email and password fields
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            // Email TextField with email.png icon
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "Email") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.email), // Replace with email.png
                        contentDescription = "Email Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified // Preserve the original image color
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, CircleShape),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = ObjectGreen1,
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            // Password TextField with password_eye.png icon
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "Password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password_eye), // Replace with password_eye.png
                        contentDescription = "Password Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified // Preserve the original image color
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, CircleShape),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = ObjectGreen1,
                    unfocusedIndicatorColor = Color.Gray
                )
            )
        }

        // Circular "Editar" button at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp, 70.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { /* Add edit action */ },
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(horizontal = 10.dp, vertical = 10.dp), // Adjust padding for text
                colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen1)
            ) {
                Text(
                    text = "Editar",
                    fontSize = 42.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}