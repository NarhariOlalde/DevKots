package com.example.devkots.uiLib

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devkots.R

@Composable
fun ForgotPassword(name: String, modifier: Modifier = Modifier) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    Image(
        painter = painterResource(R.drawable.vector_5),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .size(191.dp),
    )
    Image(
        painter = painterResource(R.drawable.vector_1),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .size(160.dp)
            .padding(start = 232.dp)
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.vector_3),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .size(350.dp)
                .padding(top = 100.dp)

        )
    }


    Column(
        modifier = modifier
    ) {
        IconButton(onClick = { /* No hace nada */ }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Regresar",
                tint = Color.White,
            )
        }
        Text(
            text = "Crear nueva contraseña",
            fontSize = 40.sp,
            modifier = Modifier.padding(
                top = 20.dp,
                start = 12.dp
            ),
            lineHeight = 40.sp,
            color = colorResource(id = R.color.white)

        )
        Text(
            text = "Tu nueva contraseña debe ser diferente a la que usaste anteriormente",
            fontSize = 20.sp,
            modifier = Modifier.padding(
                top = 85.dp,
                start = 12.dp,
                end = 12.dp
            ),
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Nueva contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.visibility_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = null
                )
            },
            singleLine = true
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.visibility_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = null
                )
            },
            singleLine = true
        )
        Column(
            modifier = Modifier.fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Button(
                onClick = { /* TODO: Handle forgot password */ },
                modifier = Modifier.padding(top = 235.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.Object_Green1)
                )
            ) {
                Text(text = "GUARDAR")
            }
        }
    }
}
