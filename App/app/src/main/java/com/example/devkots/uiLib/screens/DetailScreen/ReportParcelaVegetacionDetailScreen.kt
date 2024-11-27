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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.devkots.R
import com.example.devkots.data.AppDatabase
import com.example.devkots.data.BioReportService
import com.example.devkots.uiLib.components.EditableField
import com.example.devkots.uiLib.components.EditableFieldNumeric
import com.example.devkots.uiLib.components.createMediaStoreImageUri
import com.example.devkots.uiLib.components.handleCameraClick
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.viewmodels.Report.ReportParcelaVegetacionViewModel
import com.example.devkots.uiLib.viewmodels.ReportViewModelFactory

@Composable
fun ReportParcelaVegetacionDetailScreen(
    navController: NavController,
    status: Boolean,
    reportId: Int,
    bioReportService: BioReportService
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val parcelaVegetacionReportDao = database.parcelaVegetacionReportDao()

    val viewModel: ReportParcelaVegetacionViewModel = viewModel(
        factory = ReportViewModelFactory(
            bioReportService = bioReportService,
            parcelaVegetacionReportDao = parcelaVegetacionReportDao
        )
    )

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

    LaunchedEffect(reportId, status) {
        viewModel.loadReport(reportId, status)
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

    // Función para verificar si todos los campos obligatorios están completos
    fun isFormValid(): Boolean {
        val report = viewModel.report
        return report != null &&
                report.code.isNotEmpty() &&
                report.cuadrante.isNotEmpty() &&
                report.subcuadrante.isNotEmpty() &&
                report.habitocrecimiento.isNotEmpty() &&
                report.nombrecomun.isNotEmpty() &&
                report.placa.isNotEmpty() &&
                report.circunferencia > 0 &&
                report.distancia > 0 &&
                report.estaturabio > 0 &&
                report.altura > 0 &&
                report.weather.isNotEmpty() &&
                report.season.isNotEmpty()
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
                    text = "Cuadrante",
                    fontSize = 24.sp,
                )
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
                                    .clickable(enabled = viewModel.isEditable) { viewModel.cuadranteuno = cuadr }
                                    .background(
                                        color = if (viewModel.cuadranteuno == cuadr) Color(0xFF99CC66) else Color.White,
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
                                    .clickable(enabled = viewModel.isEditable) { viewModel.cuadrantedos = cuadr
                                    }
                                    .background(
                                        color = if (viewModel.cuadrantedos == cuadr) Color(0xFF99CC66) else Color.White,
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
                }

                Text(
                    text = "Sub-Cuadrante",
                    fontSize = 24.sp,
                )
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
                                .clickable(enabled = viewModel.isEditable) { viewModel.report = viewModel.report?.copy(subcuadrante = subcuadr) }
                                .background(
                                    color = if (viewModel.report!!.subcuadrante == subcuadr.toString()) Color(0xFF99CC66) else Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 40.dp, vertical = 16.dp)
                        ) {
                            androidx.compose.material3.Text(
                                text = subcuadr.toString(),
                                fontSize = 24.sp,
                                color = Color.Black
                            )
                        }
                    }
                }

                Text(
                    text = "Hábito de Crecimiento",
                    fontSize = 24.sp,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val habito = listOf(
                        Pair(R.drawable.arbusto, "Arbusto < 1 mt"),
                        Pair(R.drawable.arbolito, "Arbolito 1-3 mt"),
                        Pair(R.drawable.arbol, "Árbol > 3 mt")
                    )
                    habito.forEachIndexed { index, habit ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable(enabled = viewModel.isEditable) { viewModel.report = viewModel.report?.copy(habitocrecimiento = habit.second)}
                        ) {
                            Image(
                                painter = painterResource(id = habit.first),
                                contentDescription = habit.second,
                                modifier = Modifier
                                    .size(125.dp)
                                    .background(
                                        if (viewModel.report!!.habitocrecimiento == habit.second) Color(0xFF99CC66) else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            )
                            androidx.compose.material3.Text(
                                text = habit.second,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        }
                    }
                }

                EditableField("Nombre Común Especie", viewModel.report!!.nombrecomun, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(nombrecomun = it)
                }

                EditableField("Nombre Científico", viewModel.report!!.nombrecientifico ?: "", viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(nombrecientifico = it.takeIf { it.isNotEmpty() })
                }

                EditableField("Placa", viewModel.report!!.placa, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(placa = it)
                }

                EditableFieldNumeric("Circunferencia en cm", viewModel.report!!.circunferencia, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(circunferencia = it)
                }

                EditableFieldNumeric("Distancia en mt", viewModel.report!!.distancia, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(distancia = it)
                }

                EditableFieldNumeric("Estatura Biomonitor en mt", viewModel.report!!.estaturabio, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(estaturabio = it)
                }

                EditableFieldNumeric("Altura en mt", viewModel.report!!.altura, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(altura = it)
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
                                .clickable(enabled = viewModel.isEditable) { // Solo seleccionable si es editable
                                    if (viewModel.isEditable) {
                                        viewModel.report = viewModel.report?.copy(weather = weatherType)
                                    }
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
                                },
                                enabled = viewModel.isEditable
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
                            .size(width = 170.dp, height = 50.dp),
                        enabled = viewModel.isEditable
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
                            .size(width = 170.dp, height = 50.dp),
                        enabled = viewModel.isEditable
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
                        // Verifica si el photoPath no está vacío
                        if (photoPath.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Gray)
                            ) {
                                val painter = rememberAsyncImagePainter(
                                    model = photoPath,  // Aquí se pasa la ruta de la imagen
                                )
                                Image(
                                    painter = painter,
                                    contentDescription = "Image",
                                )
                                // Solo muestra el botón de eliminar si la vista es editable
                                if (viewModel.isEditable) {
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
                    }
                }

                EditableField("Observaciones", viewModel.report!!.observations, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(observations = it)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (viewModel.isEditable) {
                    Button(
                        onClick = {
                            viewModel.report = viewModel.report?.copy(cuadrante = "${viewModel.cuadranteuno}-${viewModel.cuadrantedos}")
                            viewModel.updateReport(reportId, viewModel.report!!)
                            navController.popBackStack() // Go back after successful edit
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4E7029)),
                        enabled = isFormValid() && viewModel.isEditable
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