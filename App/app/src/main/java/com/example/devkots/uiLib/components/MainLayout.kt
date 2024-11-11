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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.devkots.R
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
        backgroundColor = Color(0xFFB4D68F),
        contentColor = Color.Black,
        modifier = Modifier
            .height(120.dp)
    ) {
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("dashboard") },
            icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.inicio),
                        contentDescription = "Inicio",
                        tint = Color.Black,
                        modifier = Modifier.size(65.dp)
                    )
                    Text("Inicio", color = Color.Black, fontSize = 20.sp)
                }
            }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("search") },
            icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.busqueda),
                        contentDescription = "Búsqueda",
                        tint = Color.Black,
                        modifier = Modifier.size(65.dp)
                    )
                    Text("Búsqueda", color = Color.Black, fontSize = 20.sp)
                }
            }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("configuracion") },
            icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.configuracion),
                        contentDescription = "Configuración",
                        tint = Color.Black,
                        modifier = Modifier.size(65.dp)
                    )
                    Text("Configuración", color = Color.Black, fontSize = 20.sp)
                }
            }
        )
        BottomNavigationItem(
            selected = false,
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        cameraUri.value = createImageFile(context)
                        cameraUri.value?.let { cameraLauncher.launch(it) }
                    }

                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA
                    ) -> {
                        submissionResult = "Please allow camera access to take photos."
                    }

                    else -> {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            },
            icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(top = 55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Toma Express",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                    Text("Toma Express", color = Color.Black, fontSize = 20.sp, modifier = Modifier.padding(top = 13.dp))
                }
            }
        )
    }
}