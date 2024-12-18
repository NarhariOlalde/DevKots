package com.example.devkots.uiLib.screens.FormScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.data.AppDatabase
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.BioReportEntity
import com.example.devkots.model.LocalEntities.VariablesClimaticasReportEntity
import com.example.devkots.model.VariablesClimaticasReport
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

    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission is required for images is required to access the gallery.", Toast.LENGTH_SHORT).show()
        }
    }

    FormLayout(navController = navController){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IntroGreen)
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
        )
        {
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
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
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
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
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
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
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
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
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
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
                        val report = VariablesClimaticasReportEntity(
                            zona = zone,
                            pluviosidad = pluviosidad.toIntOrNull() ?: 0,
                            tempmax = tempmax.toIntOrNull() ?: 0,
                            humedadmax = humedadmax.toIntOrNull() ?: 0,
                            tempmin = tempmin.toIntOrNull() ?: 0,
                            nivelquebrada = nivelquebrada.toIntOrNull() ?: 0,
                            date = currentDate,
                            time = currentTime,
                            gpsLocation = gpsLocation,
                            weather = weather,
                            status = false,
                            season = season,
                            biomonitor_id = biomonitorID
                        )
                        coroutineScope.launch {
                            val database = AppDatabase.getInstance(context)
                            val faunaDao = database.variablesClimaticasReportDao()
                            val bioDao = database.bioReportDao()
                            try {
                                val formId = faunaDao.insertVariablesClimaticasReport(report)
                                val reportBio = BioReportEntity(
                                    formId = formId,
                                    date = currentDate,
                                    status = false,
                                    biomonitor_id = biomonitorID,
                                    type = "Variables Climáticas"
                                )
                                bioDao.insertReport(reportBio)
                                submissionResult = "Report saved locally successfully!"
                                zone = ""
                                pluviosidad = ""
                                tempmax = ""
                                humedadmax = ""
                                tempmin = ""
                                nivelquebrada = ""

                            } catch (e: Exception) {
                                submissionResult = "Failed to save report locally: ${e.message}"
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
            Spacer(modifier = Modifier.height(20.dp))
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