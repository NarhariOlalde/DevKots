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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.CamarasTrampaReport
import com.example.devkots.uiLib.components.FormLayout
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
fun CamarasTrampaFormScreen(
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
    var code by remember { mutableStateOf("") }
    var zone by remember { mutableStateOf("") }
    var nombreCamara by remember { mutableStateOf("") }
    var placaCamara by remember { mutableStateOf("") }
    var placaGuaya by remember { mutableStateOf("") }
    var anchoCamino by remember { mutableStateOf("") }
    var fechainstalacion by remember { mutableStateOf("") }
    var distanciaobj by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var listachequeo by remember { mutableStateOf("") }
    var photoPath by remember { mutableStateOf<Uri?>(null) }
    var observations by remember { mutableStateOf("") }
    var submissionResult by remember { mutableStateOf<String?>(null) }
    val Listachequeo = listOf(
        "Programada",
        "Memoria",
        "Prueba de gateo",
        "Instalada",
        "Letrero de cámara",
        "Prendida"
    )
    val checkedStates = remember { mutableStateListOf(false, false, false, false, false, false) }

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
    FormLayout(navController = navController){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IntroGreen)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        )
        {
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = {
                            Text("Código", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Zona",
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
                Spacer(modifier = Modifier.height(15.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = nombreCamara,
                        onValueChange = { nombreCamara = it },
                        label = {
                            Text("Nombre Cámara", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
                        value = placaCamara,
                        onValueChange = { placaCamara = it },
                        label = {
                            Text("Placa Cámara", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
                        value = placaGuaya,
                        onValueChange = { placaGuaya = it },
                        label = {
                            Text("Placa Guaya", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
                        value = anchoCamino,
                        onValueChange = { anchoCamino = it },
                        label = {
                            Text("Ancho del Camino en mts", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
                        value = fechainstalacion,
                        onValueChange = { fechainstalacion = it },
                        label = {
                            Text("Fecha de Instalación", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
                        value = distanciaobj,
                        onValueChange = { distanciaobj = it },
                        label = {
                            Text("Distancia al objetivo en mts", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
                        value = altura,
                        onValueChange = { altura = it },
                        label = {
                            Text("Altura del lente en mts", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Lista de chequeo",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Listachequeo.forEachIndexed { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = { isChecked ->
                                checkedStates[index] = isChecked
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            fontSize = 28.sp,
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Evidencias",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                        val report = CamarasTrampaReport(
                            code = code,
                            zona = zone,
                            nombrecamara = nombreCamara,
                            placacamara = placaCamara,
                            placaguaya = placaGuaya,
                            anchocamino = anchoCamino.toIntOrNull() ?: 0,
                            fechainstalacion = fechainstalacion,
                            distancia = distanciaobj.toIntOrNull() ?: 0,
                            altura = altura.toIntOrNull() ?: 0,
                            //listachequeo = checkedStates.mapIndexedNotNull { index, isChecked -> if (isChecked) Listachequeo[index] else null },
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
                            val response = RetrofitInstanceBioReport.api.submitCamarasTrampaReport(report)
                            submissionResult = if (response.isSuccessful) "Report submitted successfully!" else "Submission failed."

                            // Reset form on success
                            if (response.isSuccessful) {
                                code = ""
                                zone = ""
                                nombreCamara = ""
                                placaCamara = ""
                                placaGuaya = ""
                                anchoCamino = ""
                                fechainstalacion = ""
                                distanciaobj = ""
                                altura = ""
                                listachequeo = ""
                                photoPath = null
                                observations = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(60.dp),
                    enabled = zone.isNotEmpty() && code.isNotEmpty() && nombreCamara.isNotEmpty() && placaCamara.isNotEmpty() && placaGuaya.isNotEmpty() && anchoCamino.isNotEmpty() && fechainstalacion.isNotEmpty() && distanciaobj.isNotEmpty() && altura.isNotEmpty(),
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