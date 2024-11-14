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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
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
import com.example.devkots.model.ParcelaVegetacionReport
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
fun ParcelaVegetacionFormScreen(
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
    var cuadrante by remember { mutableStateOf("") }
    var cuadranteuno by remember { mutableStateOf("") }
    var cuadrantedos by remember { mutableStateOf("") }
    var subcuadrante by remember { mutableStateOf("") }
    var habitoCrecimiento by remember { mutableStateOf("") }
    var nombreComun by remember { mutableStateOf("") }
    var nombreCientifico by remember { mutableStateOf("") }
    var placa by remember { mutableStateOf("") }
    var circunferencia by remember { mutableStateOf("") }
    var distancia by remember { mutableStateOf("") }
    var estaturabio by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var photoPath by remember { mutableStateOf<Uri?>(null) }
    var observations by remember { mutableStateOf("") }
    var submissionResult by remember { mutableStateOf<String?>(null) }

    val habito = listOf(
        Pair(R.drawable.arbusto, "Arbusto < 1 mt"),
        Pair(R.drawable.arbolito, "Arbolito 1-3 mt"),
        Pair(R.drawable.arbol, "Árbol > 3 mt")
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
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cuadrante",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val cuadrante1 = listOf("A", "B")
                val cuadrante2 = listOf("C", "D", "E", "F", "G")
                Column{
                    cuadrante1.forEachIndexed { index, cuadr ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { cuadranteuno = cuadr}
                                .background(
                                    color = if (cuadranteuno == cuadr) Color(0xFF99CC66) else Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 45.dp, vertical = 45.dp)
                        ) {
                            Text(
                                text = cuadr,
                                fontSize = 50.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.guion),
                    contentDescription = "Guion"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column{
                    cuadrante2.forEachIndexed { index, cuadr ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { cuadrantedos = cuadr}
                                .background(
                                    color = if (cuadrantedos == cuadr) Color(0xFF99CC66) else Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 60.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = cuadr,
                                fontSize = 24.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                cuadrante = cuadranteuno + "-" + cuadrantedos
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Sub-Cuadrante",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val subcuadrantelist = listOf("1", "2", "3", "4")
                subcuadrantelist.forEachIndexed { index, subcuadr ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { subcuadrante = subcuadr.toString() }
                            .background(
                                color = if (subcuadrante == subcuadr.toString()) Color(0xFF99CC66) else Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 40.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = subcuadr.toString(),
                            fontSize = 24.sp,
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Hábito de crecimiento",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                habito.forEachIndexed { index, habit ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { habitoCrecimiento = habit.second }
                    ) {
                        Image(
                            painter = painterResource(id = habit.first),
                            contentDescription = habit.second,
                            modifier = Modifier
                                .size(125.dp)
                                .background(
                                    if (habitoCrecimiento == habit.second) Color(0xFF99CC66) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        )
                        Text(
                            text = habit.second,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = nombreComun,
                    onValueChange = { nombreComun = it },
                    label = {
                        Text("Nombre Común", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
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
                    value = nombreCientifico,
                    onValueChange = { nombreCientifico = it },
                    label = {
                        Text("Nombre Científico", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
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
            Text(
                text = "Opcional",
                fontSize = 18.sp,
                color = colorResource(id = R.color.black),
                modifier = Modifier
                    .padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = placa,
                    onValueChange = { placa = it },
                    label = {
                        Text("Placa", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
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
                    value = circunferencia,
                    onValueChange = { circunferencia = it },
                    label = {
                        Text("Circunferencia en cm", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
                    value = distancia,
                    onValueChange = { distancia = it },
                    label = {
                        Text("Distancia en mt", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
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
                    value = estaturabio,
                    onValueChange = { estaturabio = it },
                    label = {
                        Text("Estatura del biomonitor en mt", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
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
                    value = altura,
                    onValueChange = { altura = it },
                    label = {
                        Text("Altura en mt", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
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
                        val report = ParcelaVegetacionReport(
                            code = code,
                            cuadrante = cuadrante,
                            subcuadrante = subcuadrante,
                            habitocrecimiento = habitoCrecimiento,
                            nombrecomun = nombreComun,
                            nombrecientifico = nombreCientifico.takeIf { it.isNotEmpty() },
                            placa = placa,
                            circunferencia = circunferencia.toIntOrNull() ?: 0,
                            distancia = distancia.toIntOrNull() ?: 0,
                            estaturabio = estaturabio.toIntOrNull() ?: 0,
                            altura = altura.toIntOrNull() ?: 0,
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
                            val response = RetrofitInstanceBioReport.api.submitParcelaVegetacionReport(report)
                            submissionResult = if (response.isSuccessful) "Report submitted successfully!" else "Submission failed."

                            // Reset form on success
                            if (response.isSuccessful) {
                                code = ""
                                cuadrante = ""
                                subcuadrante = ""
                                habitoCrecimiento = ""
                                nombreComun = ""
                                nombreCientifico = ""
                                placa = ""
                                circunferencia = ""
                                distancia = ""
                                estaturabio = ""
                                altura = ""
                                photoPath = null
                                observations = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(60.dp),
                    enabled = code.isNotEmpty() && cuadranteuno.isNotEmpty() && cuadrantedos.isNotEmpty() && subcuadrante.isNotEmpty() && habitoCrecimiento.isNotEmpty() && nombreComun.isNotEmpty() && placa.isNotEmpty() && circunferencia.isNotEmpty() && distancia.isNotEmpty() && estaturabio.isNotEmpty() && altura.isNotEmpty(),
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