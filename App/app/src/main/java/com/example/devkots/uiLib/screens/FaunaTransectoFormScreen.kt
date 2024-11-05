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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
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
    biomonitorID: String
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

    // Static values
    val currentDate = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(Calendar.getInstance().time)
    val currentTime = SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(Calendar.getInstance().time)
    val biomonitorId = biomonitorID
    var weather by remember { mutableStateOf("") }
    val weatherOptions = listOf("Soleado", "Nublado", "Lluvioso")

    // Dropdown options
    val animalTypeOptions = listOf("mammal", "bird", "reptile", "anfib", "insect")
    val observationTypeOptions = listOf("La vio", "Huella", "Rastro", "Caceria", "Le dijeron")

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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Text("Fauna en Transecto Form", fontSize = 24.sp, color = ObjectGreen1)

        OutlinedTextField(
            value = transectoNumber,
            onValueChange = { transectoNumber = it },
            label = { Text("Numero de Transecto") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = ObjectGreen2,
                unfocusedBorderColor = ObjectGreen1
            )
        )

        DropdownMenuSelector(
            label = "Tipo de Animal",
            options = animalTypeOptions,
            selectedOption = animalType,
            onOptionSelected = { animalType = it }
        )

        OutlinedTextField(
            value = commonName,
            onValueChange = { commonName = it },
            label = { Text("Nombre Comun") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = ObjectGreen2,
                unfocusedBorderColor = ObjectGreen1
            )
        )

        OutlinedTextField(
            value = scientificName ?: "",
            onValueChange = { scientificName = it },
            label = { Text("Nombre Cientifico (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = ObjectGreen2,
                unfocusedBorderColor = ObjectGreen1
            )
        )

        OutlinedTextField(
            value = individualCount,
            onValueChange = { individualCount = it },
            label = { Text("Numero de Individuos") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = ObjectGreen2,
                unfocusedBorderColor = ObjectGreen1
            )
        )

        DropdownMenuSelector(
            label = "Tipo de Observacion",
            options = observationTypeOptions,
            selectedOption = observationType,
            onOptionSelected = { observationType = it }
        )

        // Weather Selector
        DropdownMenuSelector(
            label = "Select Weather",
            options = weatherOptions,
            selectedOption = weather,
            onOptionSelected = { weather = it }
        )

        // File Picker Buttons (Gallery and Camera)
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
                colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen2)
            ) {
                Text("Select from Gallery", color = Color.White)
            }

            Button(
                onClick = { handleCameraClick() },
                colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen2)
            ) {
                Text("Capture Photo", color = Color.White)
            }
        }

        OutlinedTextField(
            value = observations,
            onValueChange = { observations = it },
            label = { Text("Observaciones") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = ObjectGreen2,
                unfocusedBorderColor = ObjectGreen1
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    biomonitor_id = biomonitorId
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
                        photoPath = cameraUri.value
                        observations = ""
                        weather = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = transectoNumber.isNotEmpty() && animalType.isNotEmpty() && commonName.isNotEmpty() && individualCount.isNotEmpty() && observationType.isNotEmpty() && weather.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen2)
        ) {
            Text("Submit", color = Color.White)
        }

        submissionResult?.let {
            Text(it, color = if (it.contains("success")) MaterialTheme.colors.primary else MaterialTheme.colors.error)
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
