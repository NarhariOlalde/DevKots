package com.example.devkots.uiLib.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.R
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel

@Composable
fun EditProfile(
    userSessionViewModel: UserSessionViewModel,
) {
    // Define state variables for each TextField input
    var name by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Back button at the top left corner
        IconButton(
            onClick = { /* Define back action */ },
            modifier = Modifier
                .padding(12.dp)
                .size(80.dp)
        ) {
            Text(
                text = "<-",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // Row that contains a single green circle with a camera button in front
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Green circle
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .clip(CircleShape)
                        .background(ObjectGreen2)
                )

                // Camera button positioned in front of the green circle, towards the bottom end
                IconButton(
                    onClick = { /* Define action for camera button */ },
                    modifier = Modifier
                        .size(100.dp)
                        .offset(x = (-95).dp, y = 100.dp) // Adjust offset to position in front of the green circle
                        .clip(CircleShape)
                        .background(ObjectGreen1)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Camera",
                        tint = Color.White,
                        modifier = Modifier.size(70.dp)
                    )
                }
            }
        }

        // Column for the labels and input fields below the green circle
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(top = 350.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Name label and TextField
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Name",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(text = "Enter Name", fontSize = 40.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }

        // Green save button at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp, 120.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { /* Define save action */ },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 16.dp, vertical = 48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(ObjectGreen1),
                colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen1)
            ) {
                Text(
                    text = "GUARDAR",
                    fontSize = 60.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}