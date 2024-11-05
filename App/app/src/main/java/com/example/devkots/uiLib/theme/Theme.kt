package com.example.devkots.uiLib.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val IntroGreen = Color(0xFFD6E5CC)
val ObjectGreen1 = Color(0xFF4E7029)
val ObjectGreen2 = Color(0xFFA5BE00)
val ObjectGreen3 = Color(0xFF6A994E)
val ObjectGreen4 = Color(0xFFCDE4B4)
val ObjectGreen5 = Color(0xFF058D56)

private val LightColorPalette = lightColors(
    primary = ObjectGreen2,
    primaryVariant = ObjectGreen1,
    secondary = ObjectGreen3,
    background = IntroGreen,
    surface = ObjectGreen4,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun DevKotsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        content = content
    )
}