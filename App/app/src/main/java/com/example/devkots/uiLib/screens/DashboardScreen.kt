package com.example.devkots.uiLib.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.theme.IntroGreen
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.theme.ObjectGreen3
import com.example.devkots.uiLib.viewmodels.BioReportViewModel

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
            color = IntroGreen
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Greeting Text
                Text(
                    text = "Hola $userName",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = ObjectGreen1
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Placeholder Add Button
                Button(
                    onClick = { navController.navigate("report_selection") },
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen2)
                ) {
                    Text("+", fontSize = 36.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }

                // Dashboard Label
                Text(
                    text = "Dashboard",
                    fontSize = 24.sp,
                    color = ObjectGreen3,
                    modifier = Modifier.align(Alignment.Start)
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
                Spacer(modifier = Modifier.height(8.dp))
                Text("Total Reports: $totalReports", fontSize = 18.sp, color = Color.Black)
                Text("Reports (Status = true): $trueReportsCount", fontSize = 18.sp, color = Color.Black)
                Text("Reports (Status = false): $falseReportsCount", fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = ObjectGreen2,
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("dashboard") },
            icon = { Text("Inicio", color = Color.White) }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate("search") },
            icon = { Text("Busqueda", color = Color.White) }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { /* TODO: Navigate to settings screen */ },
            icon = { Text("Configuracion", color = Color.White) }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { /* TODO: Navigate to express capture screen */ },
            icon = { Text("Toma Express", color = Color.White) }
        )
    }
}