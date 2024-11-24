package com.example.devkots.uiLib.screens.DetailScreen

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.devkots.data.BioReportService
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.devkots.R
import com.example.devkots.uiLib.components.EditableField
import com.example.devkots.uiLib.components.EditableFieldNumeric
import com.example.devkots.uiLib.components.createMediaStoreImageUri
import com.example.devkots.uiLib.components.handleCameraClick
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.viewmodels.Report.ReportFaunaTransectoViewModel
import com.example.devkots.uiLib.viewmodels.ReportViewModelFactory

@Composable
fun ReportFaunaTransectoDetailScreen(
    navController: NavController,
    reportId: Int,
    bioReportService: BioReportService
) {
    val viewModel: ReportFaunaTransectoViewModel = viewModel(
        factory = ReportViewModelFactory(bioReportService)
    )
    val context = LocalContext.current
    var submissionResult by remember { mutableStateOf<String?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.addPhoto(context, it)
        }
    }

    // Lanzador para la cámara
    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraUri.value?.let {
                viewModel.addPhoto(context, it)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadReport(reportId)
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                context,
                "Permission is required for images is required to access the gallery.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Formulario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.height(100.dp),
                backgroundColor = Color(0xFF4E7029),
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        if (viewModel.loading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        } else if (viewModel.report != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("#FM$reportId", fontSize = 32.sp, color = Color(0xFF4E7029))

                EditableFieldNumeric("Número de Transecto", viewModel.report!!.transectoNumber, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(transectoNumber = it)
                }
                Text(
                    text = "Tipo de Animal",
                    fontSize = 24.sp,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val animals = listOf(
                        R.drawable.mamifero to "Mamífero",
                        R.drawable.ave to "Ave",
                        R.drawable.reptil to "Reptil",
                        R.drawable.anfibio to "Anfibio",
                        R.drawable.insecto to "Insecto"
                    )

                    animals.forEachIndexed { index, animal ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                viewModel.report = viewModel.report?.copy(animalType = animal.second)
                            }
                        ) {
                            Image(
                                painter = painterResource(id = animal.first),
                                contentDescription = animal.second,
                                modifier = Modifier
                                    .size(90.dp)
                                    .background(
                                        if (viewModel.report!!.animalType == animal.second) Color(0xFF99CC66) else Color.Transparent,
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


                EditableField("Nombre Común", viewModel.report!!.commonName, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(commonName = it)
                }

                EditableField("Nombre Científico", viewModel.report!!.scientificName ?: "", viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(scientificName = it.takeIf { it.isNotEmpty() })
                }

                EditableFieldNumeric("Número de Individuos", viewModel.report!!.individualCount, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(individualCount = it)
                }
                Text(
                    text = "Tipo de Registro",
                    fontSize = 24.sp,
                )
                Column {
                    val observationTypes = listOf("La Vió", "Huella", "Rastro", "Cacería", "Le dijeron")
                    observationTypes.forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = viewModel.report!!.observationType == type,
                                onClick = {
                                    viewModel.report = viewModel.report?.copy(observationType = type)
                                }
                            )
                            Text(
                                text = type,
                            )
                        }
                    }
                }
                Text(
                    text = "Estado del Tiempo",
                    fontSize = 24.sp,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val weatherTypes = listOf(
                        "Soleado" to R.drawable.sunny,
                        "Nublado" to R.drawable.cloudy,
                        "Lluvioso" to R.drawable.rainy
                    )

                    weatherTypes.forEach { (weatherType, iconResId) ->
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = weatherType,
                            modifier = Modifier
                                .size(90.dp)
                                .background(
                                    if (viewModel.report!!.weather == weatherType) Color(0xFF99CC66) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    viewModel.report = viewModel.report?.copy(weather = weatherType)
                                }
                                .padding(8.dp)
                        )
                    }
                }
                Text(
                    text = "Estación",
                    fontSize = 24.sp,
                )
                Column {
                    val seasons = listOf("Verano-Seco", "Invierno-Lluviosa")

                    seasons.forEach { season ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = viewModel.report!!.season == season,
                                onClick = {
                                    viewModel.report = viewModel.report?.copy(season = season)
                                }
                            )
                            Text(
                                text = season
                            )
                        }
                    }
                }
                Text(
                    text = "Evidencias",
                    fontSize = 24.sp,
                )
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    Button(
                        onClick = { galleryLauncher.launch("image/*") }     ,
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
                viewModel.report?.photoPaths?.let { photoPaths ->
                    photoPaths.forEachIndexed { index, photoPath ->
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
                        ) {
                            val painter = rememberImagePainter(
                                photoPath,
                                builder = {
                                    crossfade(true)
                                }
                            )
                            Image(
                                painter = painter,
                                contentDescription = "Image",
                            )
                            IconButton(
                                onClick = {
                                    viewModel.removePhotoAt(index)
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .clip(CircleShape)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.x_circle),
                                    contentDescription = "Eliminar imagen",
                                    tint = Color.Red,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                EditableField("Observaciones", viewModel.report!!.observations, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(observations = it)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (viewModel.isEditable) {
                    Button(
                        onClick = {
                            viewModel.updateReport(reportId, viewModel.report!!)
                            navController.popBackStack() // Go back after successful edit
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4E7029))
                    ) {
                        Text("Save Changes", color = Color.White, fontSize = 18.sp)
                    }
                }

                viewModel.errorMessage?.let {
                    Text(it, color = MaterialTheme.colors.error)
                }
            }
        }
    }
}

