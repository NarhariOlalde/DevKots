package com.example.devkots.uiLib.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import com.example.devkots.uiLib.theme.ObjectGreen3
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun FaunaTransectoFormScreen(
    navController: NavController,
    date: String,
    time: String,
    weather: String,
    biomonitorID: String,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Form fields
    var transectoNumber by remember { mutableStateOf("") }
    var animalType by remember { mutableStateOf("") }
    var commonName by remember { mutableStateOf("") }
    var scientificName by remember { mutableStateOf("") }
    var individualCount by remember { mutableStateOf("") }
    var observationType by remember { mutableStateOf("") }
    var photoPath by remember { mutableStateOf<Uri?>(null) } // no usar
    var observations by remember { mutableStateOf("") }
    var submissionResult by remember { mutableStateOf<String?>(null) } // no usar
    val animals = listOf(
        Pair(R.drawable.mamifero, "Mamífero"),
        Pair(R.drawable.ave, "Ave"),
        Pair(R.drawable.reptil, "Reptil"),
        Pair(R.drawable.anfibio, "Anfibio"),
        Pair(R.drawable.insecto, "Insecto")
    )

    val gpsLocation = "37.7749,-122.4194" // Placeholder for GPS (latitude,longitude)
    val biomonitorId = biomonitorID

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
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF99CC66))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Regresar",
                    tint = Color.Black,
                    modifier = Modifier.size(45.dp)
                )
            }
            Text(
                text = "Formulario",
                fontSize = 35.sp,
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
                        Text("Número de Transecto", fontSize = 28.sp, modifier = Modifier.align(Alignment.Center))
                    },
                    textStyle = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(100.dp),
                    singleLine = true
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
                            .size(150.dp)
                            .background(
                                if (animalType == animal.second) Color(0xFF99CC66) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    )
                    Text(
                        text = animal.second,
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Column(
            modifier = modifier
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
                    singleLine = true
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
                    singleLine = true
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
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Tipo de Observación:",
                fontSize = 35.sp,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column() {
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
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF388E3C)),
                    modifier = Modifier
                        .padding(start = 70.dp)
                        .size(width = 250.dp, height = 50.dp)
                ) {
                    Text(
                        text = "Elegir Archivo",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                Button(
                    onClick = {
                        handleCameraClick()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF388E3C)),
                    modifier = Modifier
                        .padding(start = 70.dp)
                        .size(width = 250.dp, height = 50.dp)
                ) {
                    Text(
                        text = "Tomar foto",
                        fontSize = 20.sp,
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
                            date = date,
                            time = time,
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
                            }
                        }
                    },
                    enabled = transectoNumber.isNotEmpty() && animalType.isNotEmpty() && commonName.isNotEmpty() && individualCount.isNotEmpty() && observationType.isNotEmpty() && weather.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF388E3C)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(60.dp)

                ) {
                    Text(
                        text = "ENVIAR",
                        fontSize = 28.sp,
                        color = Color.White
                    )
                }
                submissionResult?.let {
                    Text(it, color = if (it.contains("success")) MaterialTheme.colors.primary else MaterialTheme.colors.error)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

fun createImageFile(context: Context): Uri? {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val file = File(context.getExternalFilesDir(null), "JPEG_${timestamp}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

