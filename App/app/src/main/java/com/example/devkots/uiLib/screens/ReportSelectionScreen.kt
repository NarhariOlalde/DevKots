package com.example.devkots.uiLib.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.theme.ObjectGreen2
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun ReportSelectionScreen(navController: NavController,
                          modifier: Modifier = Modifier) {
    // State variables for dropdown selections
    var selectedWeather by remember { mutableStateOf("") }
    var selectedSeason by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }

    // Options for each selection
    val weatherOptions = listOf("Soleado", "Nublado", "Lluvioso")
    val seasonOptions = listOf("Verano/Seco", "Invierno/Lluviosa")
    val typeOptions = listOf(
        "Fauna en Transecto",
        "Fauna en Punto de Conteo",
        "Fauna Busqueda Libre",
        "Validacion de Cobertura",
        "Parcela de Vegetación",
        "Camaras Trampa",
        "Variables Climaticas"
    )

    val currentDate = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(Calendar.getInstance().time)
    val currentTime = SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(Calendar.getInstance().time)

    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(currentDate) }
    var time by remember { mutableStateOf(currentTime) }
    var location by remember { mutableStateOf("") }

    MainLayout(navController = navController) {
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
                    navController.navigate("dashboard")
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

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre", fontSize = 28.sp) },
                    textStyle = TextStyle(fontSize = 28.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Fecha", fontSize = 28.sp) },
                    textStyle = TextStyle(fontSize = 28.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Hora", fontSize = 28.sp) },
                    textStyle = TextStyle(fontSize = 28.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Localidad", fontSize = 28.sp) },
                    textStyle = TextStyle(fontSize = 28.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Estado del tiempo:",
                    fontSize = 35.sp,
                    color = colorResource(id = R.color.black)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sunny),
                    contentDescription = "Soleado",
                    modifier = Modifier
                        .size(150.dp)
                        .background(
                            if (selectedWeather == "Soleado") Color(0xFF99CC66) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedWeather = "Soleado" }
                        .padding(8.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.cloudy),
                    contentDescription = "Nublado",
                    modifier = Modifier
                        .size(150.dp)
                        .background(
                            if (selectedWeather == "Nublado") Color(0xFF99CC66) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedWeather = "Nublado" }
                        .padding(8.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.rainy),
                    contentDescription = "Lluvioso",
                    modifier = Modifier
                        .size(150.dp)
                        .background(
                            if (selectedWeather == "Lluvioso") Color(0xFF99CC66) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedWeather = "Lluvioso" }
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Selecciona la estación:",
                fontSize = 35.sp,
                modifier = Modifier.padding(start = 16.dp),
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedSeason == "Verano/Seco",
                        onClick = { selectedSeason = "Verano/Seco" }
                    )
                    Text(
                        text = "Verano/Seco",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedSeason == "Invierno/Lluviosa",
                        onClick = { selectedSeason = "Invierno/Lluviosa" }
                    )
                    Text(
                        text = "Invierno/Lluviosa",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            Text(
                text = "Tipo de registro:",
                fontSize = 35.sp,
                modifier = Modifier.padding(start = 16.dp),
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                typeOptions.forEach { type ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedType == type,
                            onClick = { selectedType = type }
                        )
                        Text(
                            text = type,
                            fontSize = 28.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    navController.navigate("fauna_transect_form/$date/$time/$selectedWeather")
                },
                enabled = selectedWeather.isNotEmpty() && selectedSeason.isNotEmpty() && selectedType.isNotEmpty() && name.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && location.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF388E3C)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(60.dp)
            ) {
                Text(
                    text = "Siguiente",
                    fontSize = 28.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}