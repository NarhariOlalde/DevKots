package com.example.devkots.uiLib.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.FaunaPuntoConteoReport
import com.example.devkots.model.VariablesClimaticasReport
import com.example.devkots.uiLib.theme.IntroGreen
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun VariablesClimaticasFormScreen(
    navController: NavController,
    biomonitorID: String,
    weather: String,
    season: String
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Location provider
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var gpsLocation by remember { mutableStateOf("Fetching location...") }

    // Form fields
    var zone by remember { mutableStateOf("") }
    var pluviosidad by remember { mutableStateOf("") }
    var tempmax by remember { mutableStateOf("") }
    var humedadmax by remember { mutableStateOf("") }
    var tempmin by remember { mutableStateOf("") }
    var nivelquebrada by remember { mutableStateOf("") }
    var photoPath by remember { mutableStateOf<Uri?>(null) }
    var observations by remember { mutableStateOf("") }
    var submissionResult by remember { mutableStateOf<String?>(null) }

    // Static values
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time)

    // Permission launcher for location
    val locationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            fetchLocation(fusedLocationClient, context) { location ->
                gpsLocation = location
            }
        } else {
            gpsLocation = "Location permission denied"
        }
    }

    // Request location permission if needed
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchLocation(fusedLocationClient, context) { location ->
                gpsLocation = location
            }
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // File pickers
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        photoPath = uri
    }

    // Camera launcher
    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoPath = cameraUri.value // Set the captured image URI as the photoPath
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission is required for images is required to access the gallery.", Toast.LENGTH_SHORT).show()
        }
    }

    // Camera button onClick handler
    fun handleCameraClick() {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted, proceed with taking a photo
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
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IntroGreen)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.navigate("dashboard")
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Regresar",
                    tint = Color.Black,
                    modifier = Modifier.size(45.dp)
                )
            }
            Text(
                text = "Formulario",
                fontSize = 48.sp,
                lineHeight = 48.sp,
                color = colorResource(id = R.color.black)
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Opciones",
                    tint = Color.Black,
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Zona:",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                val registrationTypes1 = listOf("Bosque", "Arreglo Agroforestal", "Cultivos Transitorios", "Cultivos Permanentes")
                registrationTypes1.forEach { type ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = zone == type,
                            onClick = { zone = type }
                        )
                        Text(
                            text = type,
                            fontSize = 28.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = pluviosidad,
                onValueChange = { pluviosidad = it },
                label = {
                    Text("Pluviosidad en mm", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
                },
                textStyle = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(100.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ObjectGreen2,
                    unfocusedBorderColor = ObjectGreen1
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = tempmax,
                onValueChange = { tempmax = it },
                label = {
                    Text("Temperatura máxima", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
                },
                textStyle = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(100.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ObjectGreen2,
                    unfocusedBorderColor = ObjectGreen1
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = humedadmax,
                onValueChange = { humedadmax = it },
                label = {
                    Text("Humedad máxima", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
                },
                textStyle = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(100.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ObjectGreen2,
                    unfocusedBorderColor = ObjectGreen1
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = tempmin,
                onValueChange = { tempmin = it },
                label = {
                    Text("Temperatura mínima", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
                },
                textStyle = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(100.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ObjectGreen2,
                    unfocusedBorderColor = ObjectGreen1
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = nivelquebrada,
                onValueChange = { nivelquebrada = it },
                label = {
                    Text("Nivel de Quebrada en mts", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
                },
                textStyle = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(100.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ObjectGreen2,
                    unfocusedBorderColor = ObjectGreen1
                )
            )
        }
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        navController.navigate("report_selection")
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF388E3C)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .height(60.dp)
                ) {
                    Text(
                        text = "ATRAS",
                        fontSize = 28.sp,
                        color = Color.White
                    )
                }
                Button(
                    onClick = {
                        val report = VariablesClimaticasReport(
                            zona = zone,
                            pluviosidad = pluviosidad,
                            tempmax = tempmax,
                            humedadmax = humedadmax,
                            tempmin = tempmin,
                            nivelquebrada = nivelquebrada,
                            photoPath = photoPath?.toString(),
                            observations = observations,
                            date = currentDate,
                            time = currentTime,
                            gpsLocation = gpsLocation,
                            weather = weather,
                            status = false,
                            season = season,
                            biomonitor_id = biomonitorID
                        )

                        coroutineScope.launch {
                            val response = RetrofitInstanceBioReport.api.submitVariablesClimaticasReport(report)
                            submissionResult = if (response.isSuccessful) "Report submitted successfully!" else "Submission failed."

                            // Reset form on success
                            if (response.isSuccessful) {
                                zone = ""
                                pluviosidad = ""
                                tempmax = ""
                                humedadmax = ""
                                tempmin = ""
                                nivelquebrada = ""
                                photoPath = null
                                observations = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(60.dp),
                    enabled = zone.isNotEmpty() && pluviosidad.isNotEmpty() && tempmax.isNotEmpty() && humedadmax.isNotEmpty() && tempmin.isNotEmpty() && nivelquebrada.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF388E3C)),

                    ) {
                    Text(
                        text = "ENVIAR",
                        fontSize = 28.sp,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            submissionResult?.let {
                Text(it, color = if (it.contains("success")) MaterialTheme.colors.primary else MaterialTheme.colors.error)
            }
        }
    }

// Function to fetch location
private fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    onLocationFetched: (String) -> Unit
) {
    // Check if location permission is granted
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    onLocationFetched("$latitude,$longitude")
                } else {
                    onLocationFetched("Unable to fetch location")
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to fetch location", Toast.LENGTH_SHORT).show()
                onLocationFetched("Error fetching location")
            }
        } catch (e: SecurityException) {
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show()
            onLocationFetched("Permission denied")
        }
    } else {
        // Permission not granted, notify the caller
        Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show()
        onLocationFetched("Permission denied")
    }
}