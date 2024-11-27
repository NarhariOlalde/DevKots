package com.example.devkots.uiLib.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.theme.*
import com.example.devkots.uiLib.viewmodels.BioReportViewModel
import com.example.devkots.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.devkots.uiLib.theme.Black
import com.example.devkots.uiLib.theme.ObjectGreen1
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import android.util.Base64
import coil3.compose.rememberAsyncImagePainter


@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: BioReportViewModel,
    userName: String,
    userProfilePicture: String?,
    biomonitorId: String
) {
    // Trigger data fetch on screen load
    LaunchedEffect(Unit) {
        viewModel.fetchReportsForUser(biomonitorId)
    }

    // Collect report data from the ViewModel
    val totalReports by viewModel.totalReports.collectAsState(initial = 0)
    val trueReportsCount by viewModel.trueReportsCount.collectAsState(initial = 0)
    val falseReportsCount by viewModel.falseReportsCount.collectAsState(initial = 0)
    val trueStatusPercentage by viewModel.trueStatusPercentage.collectAsState(initial = 0.0)

    MainLayout(navController = navController) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = White
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.TopStart),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TopBar(modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp),
                        navController = navController,
                        userProfilePicture = userProfilePicture
                    )

                    // Placeholder Add Button
                    Button(
                        onClick = { navController.navigate("report_selection") },
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen1)
                    ) {
                        Text("+", fontSize = 36.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    // Dashboard Label
                    Text(
                        modifier = Modifier,
                        text = "Reportes Activos",
                        color = ObjectGreen1,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Percentage of True Reports
                    CircularProgressIndicatorWithNumber(
                        percentage = trueStatusPercentage.toFloat(),
                        modifier = Modifier.size(200.dp)
                    )


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(top = 40.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {


                        BottomReportInfoPanel(
                            totalReports = trueReportsCount,
                            color = ObjectGreen2,
                            text = "Subidos",
                            fontSize = 32
                        )
                        BottomReportInfoPanel(
                            totalReports = totalReports,
                            color = Color.Black,
                            text = "En total",
                            fontSize = 32
                        )
                        BottomReportInfoPanel(
                            totalReports = falseReportsCount,
                            color = Color.Red,
                            text = "Guardados",
                            fontSize = 32
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    userProfilePicture: String? = null
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        Image(
            painter = painterResource(id = R.drawable.vector_4),
            contentDescription = "Top Background",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillBounds,
        )
        //logo
        Image(
            painter = painterResource(id = R.drawable.awaq_logo_black),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopStart)
                .padding(start = 40.dp)
        )

        // Greeting Text
        Text(
            text = "Dashboard",
            fontSize = 60.sp,
            fontWeight = FontWeight.Black,
            color = Black,
            modifier = Modifier.align(Alignment.Center)
        )

        // Profile Icon
        //rounded
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 44.dp, top = 21.dp) // Apply padding to the container
                .size(57.dp) // Set the base size
                .aspectRatio(1f) // Ensures the content inside maintains a 1:1 aspect ratio
        ) {
            Card(
                modifier = Modifier
                    .clickable { navController.navigate("configuracion") }
                    .align(Alignment.TopEnd),
                shape = CircleShape
            ) {
                val painter = if (userProfilePicture.isNullOrEmpty()) {
                    painterResource(id = R.drawable.profile_placeholder)
                } else {
                    val decodedBytes = Base64.decode(userProfilePicture, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    rememberAsyncImagePainter(bitmap)
                }
                Image(
                    //replace with profile image later
                    painter = painter,
                    contentDescription = "Profile Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    }
}

@Composable
fun CircularProgressIndicatorWithNumber(
    percentage: Float,
    modifier: Modifier = Modifier
) {
    val strokeWidth = 20.dp
    val circleSize = 200.dp

    Box(modifier = modifier.size(circleSize), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(circleSize)) {
            // Draw the background circle
            drawCircle(
                color = IntroGreen,
                radius = size.minDimension / 2,
                style = Stroke(strokeWidth.toPx())
            )

            // Draw the progress arc
            drawArc(
                color = ObjectGreen1,
                startAngle = -90f,
                sweepAngle = percentage * 360f / 100f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height),
                topLeft = Offset(0f, 0f)
            )
        }

        Text(
            text = "${percentage.toInt()}%",
            color = ObjectGreen1,
            fontSize = 50.sp,
            fontWeight = FontWeight.Black
        )

    }
}

@Composable
fun BottomReportInfoPanel(
    modifier: Modifier = Modifier,
    totalReports: Int,
    color: Color,
    text: String,
    fontSize: Int
){
    Column(
        modifier = modifier
            .width(175.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "$totalReports", fontSize = fontSize.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Text(text, fontSize = fontSize.sp, color = color, fontWeight = FontWeight.Bold)
    }
}
