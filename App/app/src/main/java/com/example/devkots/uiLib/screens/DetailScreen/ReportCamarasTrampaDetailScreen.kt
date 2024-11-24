package com.example.devkots.uiLib.screens.DetailScreen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.devkots.R
import com.example.devkots.data.BioReportService
import com.example.devkots.uiLib.components.EditableField
import com.example.devkots.uiLib.components.EditableFieldNumeric
import com.example.devkots.uiLib.components.createMediaStoreImageUri
import com.example.devkots.uiLib.components.handleCameraClick
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.viewmodels.Report.ReportCamarasTrampaViewModel
import com.example.devkots.uiLib.viewmodels.ReportViewModelFactory

@Composable
fun ReportCamarasTrampaDetailScreen(
    navController: NavController,
    reportId: Int,
    bioReportService: BioReportService
) {
    val viewModel: ReportCamarasTrampaViewModel = viewModel(
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
    val listachequeo = listOf("Programada", "Memoria", "Prueba de gateo", "Instalada", "Letrero de cámara", "Prendida")

    // Estado para los elementos seleccionados
    val selectedItems = remember { mutableStateListOf<String>() }

    // Cargar datos del reporte al inicio
    LaunchedEffect(Unit) {
        viewModel.loadReport(reportId)
    }

    // Sincronizar selectedItems con los valores del reporte cuando cambie
    LaunchedEffect(viewModel.report) {
        viewModel.report?.listachequeo?.let { items ->
            selectedItems.clear()
            selectedItems.addAll(items)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission is required for images is required to access the gallery.", Toast.LENGTH_SHORT).show()
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

                EditableField("Código", viewModel.report!!.code, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(code = it)
                }

                Text(
                    text = "Zona",
                    fontSize = 24.sp
                )

                Column {
                    val zoneTypes = listOf("Bosque", "Arreglo Forestal", "Cultivos Transitorios", "Cultivos Permanentes")
                    zoneTypes.forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = viewModel.report!!.zona == type,
                                onClick = {
                                    viewModel.report = viewModel.report?.copy(zona = type)
                                }
                            )
                            Text(
                                text = type,
                            )
                        }
                    }
                }

                EditableField("Nombre Cámara", viewModel.report!!.nombrecamara, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(nombrecamara = it)
                }

                EditableField("Placa Cámara", viewModel.report!!.placacamara, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(placacamara = it)
                }

                EditableField("Placa Guaya", viewModel.report!!.placaguaya, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(placaguaya = it)
                }

                EditableFieldNumeric("Ancho del Camino en mts", viewModel.report!!.anchocamino, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(anchocamino = it)
                }

                EditableField("Fecha de Instalación", viewModel.report!!.fechainstalacion, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(fechainstalacion = it)
                }

                EditableFieldNumeric("Distancia al objetivo en mts", viewModel.report!!.distancia, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(distancia = it)
                }

                EditableFieldNumeric("Altura del lente en mts", viewModel.report!!.altura, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(altura = it)
                }

                Text(
                    text = "Lista de chequeo",
                    fontSize = 24.sp,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    listachequeo.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = selectedItems.contains(item),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        if (!selectedItems.contains(item)) {
                                            selectedItems.add(item)
                                        }
                                    } else {
                                        selectedItems.remove(item)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item,
                                color = Color.Black
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

                Spacer(modifier = Modifier.height(16.dp))

                if (viewModel.isEditable) {
                    Button(
                        onClick = {
                            viewModel.report = viewModel.report?.copy(listachequeo = selectedItems)
                            viewModel.updateReport(reportId, viewModel.report!!)
                            navController.popBackStack()
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