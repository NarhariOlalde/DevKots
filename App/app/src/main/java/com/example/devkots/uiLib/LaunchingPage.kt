package com.example.devkots.uiLib


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.devkots.R
import com.example.devkots.ui.theme.DevKotsTheme
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun LaunchingPagePreview() {
    DevKotsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(R.color.Intro_Green)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text (
                    text = "AWAQ-BIO",
                    fontSize = 80.sp,
                    lineHeight = 116.sp,
                    textAlign = TextAlign.Center,

                    )
                Image(
                    painter = painterResource(R.drawable.introimage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.5F
                    )
                FilledButtonExample(onClick = {

                })
                }
            }

        }
    }


@Composable
fun FilledButtonExample(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text("Iniciar Sesion")
    }
}
