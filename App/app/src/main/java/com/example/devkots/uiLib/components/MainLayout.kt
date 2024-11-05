package com.example.devkots.uiLib.components

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.devkots.uiLib.screens.createImageFile
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


// Camera button onClick handler

@Composable
fun BottomNavigationBar(navController: NavController) {
    val context = LocalContext.current
    var submissionResult by remember { mutableStateOf<String?>(null) }
    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission is required for images is required to access the gallery.", Toast.LENGTH_SHORT).show()
        }
    }




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
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> { // Permission is already granted, proceed with taking a photo
                        cameraUri.value = createImageFile(context)
                        cameraUri.value?.let { cameraLauncher.launch(it) }
                    }
                    ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.CAMERA) -> {
                        // Explain to the user why you need the camera permission (if needed)
                        submissionResult = "Please allow camera access to take photos."
                    }
                    else -> {
                        // Request the permission
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }

                }
                      },
            icon = { Text("Toma Express", color = Color.White) }
        )
    }
}