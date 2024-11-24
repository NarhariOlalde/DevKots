package com.example.devkots.uiLib.screens.FormScreen

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.data.AppDatabase
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.BioReportEntity
import com.example.devkots.model.FaunaTransectoReport
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity
import com.example.devkots.uiLib.components.FormLayout
import com.example.devkots.uiLib.components.copyUriToExternalStorage
import com.example.devkots.uiLib.components.createMediaStoreImageUri
import com.example.devkots.uiLib.components.handleCameraClick
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
    var observations by remember { mutableStateOf("") }
    var submissionResult by remember { mutableStateOf<String?>(null) }
    val animals = listOf(
        Pair(R.drawable.mamifero, "Mamífero"),
        Pair(R.drawable.ave, "Ave"),
        Pair(R.drawable.reptil, "Reptil"),
        Pair(R.drawable.anfibio, "Anfibio"),
        Pair(R.drawable.insecto, "Insecto")
    )
    val photoPaths = remember { mutableStateListOf<Uri>() }

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

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            if (photoPaths.size < 5) { // Limita a 5 imágenes
                photoPaths.add(it)
            } else {
                Toast.makeText(context, "Máximo de 5 imágenes alcanzado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Camera launcher
    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraUri.value?.let {
                if (photoPaths.size < 5) {
                    photoPaths.add(it)
                } else {
                    Toast.makeText(context, "Máximo de 5 imágenes alcanzado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission is required for images is required to access the gallery.", Toast.LENGTH_SHORT).show()
        }
    }

    FormLayout(navController = navController) {
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
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
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
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
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
                            handleCameraClick(
                                context = context,
                                cameraUri = cameraUri,
                                cameraLauncher = cameraLauncher,
                                permissionLauncher = permissionLauncher,
                                submissionResult = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            )
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
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    photoPaths.forEachIndexed { index, uri ->
                        val newUri = copyUriToExternalStorage(context, uri) ?: uri

                        // Actualizar la URI en la lista para que refleje el cambio
                        photoPaths[index] = newUri

                        // Agregar código para mostrar la imagen
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = newUri.lastPathSegment ?: "Archivo desconocido",
                                modifier = Modifier.weight(1f),
                                fontSize = 16.sp
                            )

                            // Botón para eliminar archivo
                            IconButton(
                                onClick = { photoPaths.removeAt(index) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.x_circle),
                                    contentDescription = "Eliminar archivo",
                                    tint = Color.Red
                                )
                            }
                        }
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
                            val report = FaunaTransectoReportEntity(
                                transectoNumber = transectoNumber.toIntOrNull() ?: 0,
                                animalType = animalType,
                                commonName = commonName,
                                scientificName = scientificName.takeIf { it.isNotEmpty() },
                                individualCount = individualCount.toIntOrNull() ?: 0,
                                observationType = observationType,
                                photoPaths = photoPaths.map { it.toString() }, // Convertir a String
                                observations = observations,
                                date = currentDate,
                                time = currentTime,
                                gpsLocation = gpsLocation,
                                weather = weather,
                                status = false,
                                season = season,
                                biomonitor_id = biomonitorID
                            )

                            val reporttemporal = FaunaTransectoReport(
                                transectoNumber = transectoNumber.toIntOrNull() ?: 0,
                                animalType = animalType,
                                commonName = commonName,
                                scientificName = scientificName.takeIf { it.isNotEmpty() },
                                individualCount = individualCount.toIntOrNull() ?: 0,
                                observationType = observationType,
                                photoPaths = photoPaths.map { it.toString() },
                                observations = observations,
                                date = currentDate,
                                time = currentTime,
                                gpsLocation = gpsLocation,
                                weather = weather,
                                status = false,
                                season = season,
                                biomonitor_id = biomonitorID
                            )

                            val reportBio = BioReportEntity(
                                date = currentDate,
                                status = false,
                                biomonitor_id = biomonitorID,
                                type = "Fauna en Transecto"
                            )

                            coroutineScope.launch {
                                val database = AppDatabase.getInstance(context)
                                val faunaDao = database.faunaTransectoReportDao()
                                val bioDao = database.bioReportDao()
                                val response = RetrofitInstanceBioReport.api.submitFaunaTransectoReport(reporttemporal)
                                try {
                                    faunaDao.insertFaunaTransectoReport(report)
                                    bioDao.insertReport(reportBio)
                                    submissionResult = "Report saved locally successfully!"
                                    transectoNumber = ""
                                    animalType = ""
                                    commonName = ""
                                    scientificName = ""
                                    individualCount = ""
                                    observationType = ""
                                    photoPaths.clear()
                                    observations = ""

                                } catch (e: Exception) {
                                    submissionResult = "Failed to save report locally: ${e.message}"
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
