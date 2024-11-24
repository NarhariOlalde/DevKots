package com.example.devkots.uiLib.components

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.uiLib.theme.IntroGreen
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainLayout(navController: NavController, content: @Composable () -> Unit) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            content()
        }
    }
}

@Composable
fun FormLayout(navController: NavController, content: @Composable () -> Unit) {
    Scaffold(
        backgroundColor = IntroGreen,
        topBar = { TopNavigationBar(navController)}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            content()
        }
    }
}


@Composable
fun TopNavigationBar(
    navController: NavController,
    topText : String = "Formulario"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB4D68F))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.navigate("dashboard")
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Regresar",
                    tint = Color.Black,
                    modifier = Modifier.size(45.dp)
                )
            }
            Text(
                text = topText,
                fontSize = 48.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.SemiBold,
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
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val context = LocalContext.current
    var submissionResult by remember { mutableStateOf<String?>(null) }
    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Notificar a la galería sobre la nueva imagen
            cameraUri.value?.let { uri ->
                Toast.makeText(context, "Imagen guardada en Fotos.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si la captura falla, elimina la entrada en MediaStore
            cameraUri.value?.let { uri ->
                context.contentResolver.delete(uri, null, null)
                Toast.makeText(context, "Error al capturar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Se necesita permiso para acceder a la galería", Toast.LENGTH_SHORT).show()
        }
    }
    BottomNavigation(
        backgroundColor = Color(0xFFB4D68F),
        contentColor = Color.Black,
        modifier = Modifier
            .height(120.dp)
    ) {
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("dashboard") },
            icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.inicio),
                        contentDescription = "Inicio",
                        tint = Color.Black,
                        modifier = Modifier.size(65.dp)
                    )
                    Text("Inicio", color = Color.Black, fontSize = 20.sp)
                }
            }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("search") },
            icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.busqueda),
                        contentDescription = "Búsqueda",
                        tint = Color.Black,
                        modifier = Modifier.size(65.dp)
                    )
                    Text("Búsqueda", color = Color.Black, fontSize = 20.sp)
                }
            }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("configuracion") },
            icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.configuracion),
                        contentDescription = "Configuración",
                        tint = Color.Black,
                        modifier = Modifier.size(65.dp)
                    )
                    Text("Configuración", color = Color.Black, fontSize = 20.sp)
                }
            }
        )
        BottomNavigationItem(
            selected = false,
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        val photoUri = createMediaStoreImageUri(context)
                        if (photoUri != null) {
                            cameraUri.value = photoUri
                            cameraLauncher.launch(photoUri)
                        } else {
                            Toast.makeText(context, "Error al crear entrada para la foto.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA
                    ) -> {
                        Toast.makeText(context, "Se necesita permiso para tomar fotos", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            },
            icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(top = 55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Toma Express",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                    Text("Toma Express", color = Color.Black, fontSize = 20.sp, modifier = Modifier.padding(top = 13.dp))
                }
            }
        )
    }
}

fun createMediaStoreImageUri(context: Context): Uri? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val customName = "IMG_$timeStamp"

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$customName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}

@Composable
fun EditableField(
    label: String,
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(label, color = Color.Black, fontSize = 24.sp)
        if (isEditable) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFA5BE00),
                    unfocusedBorderColor = Color(0xFF6A994E)
                )
            )
        } else {
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun EditableFieldNumeric(
    label: String,
    value: Int,
    isEditable: Boolean,
    onValueChange: (Int) -> Unit
) {
    var textValue by remember { mutableStateOf(value.toString()) } // Mantén el valor como String

    Column {
        Text(label, color = Color.Black, fontSize = 24.sp)
        if (isEditable) {
            OutlinedTextField(
                value = textValue,
                onValueChange = { newValue ->
                    textValue = newValue // Actualiza el estado local primero
                    val intValue = newValue.toIntOrNull()
                    if (intValue != null) {
                        onValueChange(intValue) // Solo actualiza si es un número válido
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFA5BE00),
                    unfocusedBorderColor = Color(0xFF6A994E)
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        } else {
            Text(
                text = value.toString(),
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}



fun copyUriToExternalStorage(context: Context, uri: Uri): Uri? {
    return try {
        val newUri = if (uri.toString().startsWith("content://media/picker_get_content/")) {
            val mediaId = uri.lastPathSegment?.toLongOrNull()
            mediaId?.let {
                Uri.parse("content://media/external/images/media/$mediaId")
            }
        } else {
            uri
        }

        newUri
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun handleCameraClick(
    context: Context,
    cameraUri: MutableState<Uri?>,
    cameraLauncher: ActivityResultLauncher<Uri>,
    permissionLauncher: ActivityResultLauncher<String>,
    submissionResult: (String) -> Unit
) {
    when {
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
            // Permission is already granted, proceed with taking a photo
            cameraUri.value = createMediaStoreImageUri(context)
            cameraUri.value?.let { cameraLauncher.launch(it) }
        }
        ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.CAMERA) -> {
            // Explain to the user why you need the camera permission (if needed)
            submissionResult("Please allow camera access to take photos.")
        }
        else -> {
            // Request the permission
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}






