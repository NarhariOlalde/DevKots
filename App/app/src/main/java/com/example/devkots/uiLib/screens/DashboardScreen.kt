package com.example.devkots.uiLib.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.theme.*
import com.example.devkots.uiLib.viewmodels.BioReportViewModel
import com.example.devkots.R

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: BioReportViewModel,
    userName: String,
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
                Image(
                    painter = painterResource(id = R.drawable.vector_4_1),
                    contentDescription = "vector_4.1",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.23f)
                        .align(Alignment.TopCenter)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .align(Alignment.TopStart),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Greeting Text
                    Text(
                        text = "Hola, $userName",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Placeholder Add Button
                    Button(
                        onClick = { navController.navigate("report_selection") },
                        modifier = Modifier
                            .size(100.dp)
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen1)
                    ) {
                        Text("+", fontSize = 36.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    // Dashboard Label
                    Text(
                        text = "Dashboard",
                        fontSize = 24.sp,
                        color = Black,
                        modifier = Modifier.align(Alignment.Start) // Alinea el texto "Dashboard" a la izquierda
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Percentage of True Reports
                    Text(
                        text = "${trueStatusPercentage.toInt()}% of reports are active",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = ObjectGreen1
                    )

                    // Report Stats
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Total Reports: $totalReports", fontSize = 24.sp, color = Color.Black)
                    Text("Reports (Status = true): $trueReportsCount", fontSize = 24.sp, color = Color.Black)
                    Text("Reports (Status = false): $falseReportsCount", fontSize = 24.sp, color = Color.Black)
                }
            }
        }
    }
}

