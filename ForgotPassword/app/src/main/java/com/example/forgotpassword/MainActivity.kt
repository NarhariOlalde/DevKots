package com.example.forgotpassword

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forgotpassword.ui.theme.ForgotPasswordTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForgotPasswordTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier.padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        TopMessage(modifier = Modifier.padding(innerPadding))
                        Text(
                            text = "Por favor escribe tu email para poder recibir un código de verificación.",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        MailButton(modifier = Modifier.paddingFromBaseline(top = 50.dp))
                        //add an image at the bottom
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 16.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Image(
                                painter = painterResource(R.drawable.lightgreenbottom),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                            )
                            SendButton(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MailButton(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("example@gmail.com") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Email") },
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TopMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.top),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = "Olvidaste la contraseña?",
            color = colorResource(R.color.white),
            fontSize = 40.sp,
            lineHeight = 46.sp,
            fontWeight =  androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SendButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.Object_Green1),
        ),
        modifier = modifier,
    ) {
        Text(
            text = "Enviar",
            color = colorResource(R.color.white),
            fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}