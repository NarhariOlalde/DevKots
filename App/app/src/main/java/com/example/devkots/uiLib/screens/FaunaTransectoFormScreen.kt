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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
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
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.FaunaTransectoReport
import com.example.devkots.uiLib.theme.IntroGreen
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun FaunaTransectoFormScreen(
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
    var transectoNumber by remember { mutableStateOf("") }
    var animalType by remember { mutableStateOf("") }
    var commonName by remember { mutableStateOf("") }
    var scientificName by remember { mutableStateOf("") }
    var individualCount by remember { mutableStateOf("") }
    var observationType by remember { mutableStateOf("") }
    var photoPath by remember { mutableStateOf<Uri?>(null) }
    var observations by remember { mutableStateOf("") }
    var submissionResult by remember { mutableStateOf<String?>(null) }
    val animals = listOf(
        Pair(R.drawable.mamifero, "Mamífero"),
        Pair(R.drawable.ave, "Ave"),
        Pair(R.drawable.reptil, "Reptil"),
        Pair(R.drawable.anfibio, "Anfibio"),
        Pair(R.drawable.insecto, "Insecto")
    )

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
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = transectoNumber,
                    onValueChange = { transectoNumber = it },
                    label = {
                        Text(
                            "Número de Transecto",
                            fontSize = 28.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    },
                    textStyle = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(100.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = ObjectGreen2,
                        unfocusedBorderColor = ObjectGreen1
                )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Tipo de Animal:",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            animals.forEachIndexed { index, animal ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { animalType = animal.second }
                ) {
                    Image(
                        painter = painterResource(id = animal.first),
                        contentDescription = animal.second,
                        modifier = Modifier
                            .size(90.dp)
                            .background(
                                if (animalType == animal.second) Color(0xFF99CC66) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    )
                    Text(
                        text = animal.second,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = commonName,
                    onValueChange = { commonName = it },
                    label = {
                        Text("Nombre Común", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
                    },
                    textStyle = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .height(100.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = ObjectGreen2,
                        unfocusedBorderColor = ObjectGreen1
                    )
                )
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = scientificName,
                    onValueChange = { scientificName = it },
                    label = {
                        Text("Nombre Científico", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
                    },
                    textStyle = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .height(100.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = ObjectGreen2,
                        unfocusedBorderColor = ObjectGreen1
                    )
                )
            }
            Text(
                text = "Opcional",
                fontSize = 18.sp,
                color = colorResource(id = R.color.black),
                modifier = Modifier
                    .padding(start = 8.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = individualCount,
                    onValueChange = { individualCount = it },
                    label = {
                        Text("Número de Individuos", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
                    },
                    textStyle = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .height(100.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = ObjectGreen2,
                        unfocusedBorderColor = ObjectGreen1
                    )
                )
            }
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Tipo de Observación:",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                val registrationTypes = listOf("La Vió", "Huella", "Rastro", "Cacería", "Le dijeron")
                registrationTypes.forEach { type ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = observationType == type,
                            onClick = { observationType = type }
                        )
                        Text(
                            text = type,
                            fontSize = 28.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Evidencias",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            // Check READ_MEDIA_IMAGES permission on Android 13+
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                                galleryLauncher.launch("image/*")
                            } else {
                                // Request READ_MEDIA_IMAGES permission
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }
                        } else {
                            // No need for READ_MEDIA_IMAGES on older Android versions
                            galleryLauncher.launch("image/*")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen2),
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .size(width = 170.dp, height = 50.dp)
                ) {
                    Text(
                        text = "Elegir Archivo",
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Button(
                    onClick = {
                        handleCameraClick()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF388E3C)),
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .size(width = 170.dp, height = 50.dp)
                ) {
                    Text(
                        text = "Tomar foto",
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = observations,
                onValueChange = { observations = it },
                label = {
                    Text("Observaciones", fontSize = 28.sp)
                },
                textStyle = TextStyle(fontSize = 28.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(200.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                        val report = FaunaTransectoReport(
                            transectoNumber = transectoNumber.toIntOrNull() ?: 0,
                            animalType = animalType,
                            commonName = commonName,
                            scientificName = scientificName.takeIf { it.isNotEmpty() },
                            individualCount = individualCount.toIntOrNull() ?: 0,
                            observationType = observationType,
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
                            val response = RetrofitInstanceBioReport.api.submitFaunaTransectoReport(report)
                            submissionResult = if (response.isSuccessful) "Report submitted successfully!" else "Submission failed."

                            // Reset form on success
                            if (response.isSuccessful) {
                                transectoNumber = ""
                                animalType = ""
                                commonName = ""
                                scientificName = ""
                                individualCount = ""
                                observationType = ""
                                photoPath = null
                                observations = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(60.dp),
                    enabled = transectoNumber.isNotEmpty() && animalType.isNotEmpty() && commonName.isNotEmpty() && individualCount.isNotEmpty() && observationType.isNotEmpty() && weather.isNotEmpty(),
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
}

fun createImageFile(context: Context): Uri? {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val file = File(context.getExternalFilesDir(null), "JPEG_${timestamp}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
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
