package com.example.devkots.uiLib.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.uiLib.theme.*

@Composable
fun LoginSignupScreen(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        color = IntroGreen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "AWAQ-BIO",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = ObjectGreen1
            )

            Image(
                painter = painterResource(id = R.drawable.introimage),
                contentDescription = "Nature Image",
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomButtonLS(text = "Login", backgroundColor = ObjectGreen1) {
                    navController.navigate("login")
                }
                CustomButtonLS(text = "Signup", backgroundColor = ObjectGreen1) {
                    navController.navigate("signup")
                }
            }
        }
    }
}

@Composable
fun CustomButtonLS(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}