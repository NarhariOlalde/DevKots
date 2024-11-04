package com.example.devkots.uiLib.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.devkots.uiLib.theme.ObjectGreen2

@Composable
fun MainLayout(navController: NavController, content: @Composable () -> Unit) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            content()
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = ObjectGreen2,
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("dashboard") },
            icon = { Text("Inicio", color = Color.White) }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("search") },
            icon = { Text("Busqueda", color = Color.White) }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("configuracion") },
            icon = { Text("Configuracion", color = Color.White) }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { /* TODO: Navigate to express capture screen */ },
            icon = { Text("Toma Express", color = Color.White) }
        )
    }
}