package com.example.devkots.uiLib.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.compose.rememberNavController


@Composable
fun ProfileScreen(
    userSessionViewModel: UserSessionViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Green rectangle in the background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
                .align(Alignment.TopCenter)
                .background(ObjectGreen1)
        )

        // Back button at the top left corner, in front of the green rectangle
        IconButton(
            onClick = { /* Define back action */ },
            modifier = Modifier
                .padding(16.dp)
                .size(80.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(ObjectGreen1)
        ) {
            Text(
                text = "<-",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Container to elevate both the circle and the text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 115.dp),
            contentAlignment = Alignment.Center
        ) {
            // Row with circle on the left and texts on the right
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // White circle on the left
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Column with main text and smaller text below it
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start

                ) {
                    // Main text
                    Text(
                        text = "Profile User",
                        fontSize = 70.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Smaller text below
                    Text(
                        text = "Active since: ",
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                }
            }
        }

        // Email and Password input fields with placeholder image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 350.dp)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp) // Increase space between fields
        ) {
            // States for email and password fields
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            // Email TextField with increased size
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "Email", fontSize = 50.sp) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = "Email Icon",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(end = 15.dp),
                        tint = Color.Unspecified
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, CircleShape)
                    .height(90.dp), // Adjust the height of the TextField
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = ObjectGreen1,
                    unfocusedIndicatorColor = Color.Gray
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 50.sp) // Increase text input size
            )

            // Password TextField with increased size
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "Password", fontSize = 50.sp) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password_eye),
                        contentDescription = "Password Icon",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(end = 15.dp),
                        tint = Color.Unspecified
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, CircleShape)
                    .height(90.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = ObjectGreen1,
                    unfocusedIndicatorColor = Color.Gray
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 50.sp)
            )
        }

        // Circular "Editar" card at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp, 70.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {navController.navigate("editProfile")} // Handle navigation
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                backgroundColor = ObjectGreen1,
                elevation = 8.dp
            ) {
                Text(
                    text = "Editar",
                    fontSize = 90.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp) // Padding inside the card
                )
            }
        }
    }
}
