package com.example.devkots.uiLib.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.model.BioReport
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.viewmodels.BioReportViewModel
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.theme.ObjectGreen1

@Composable
fun SearchScreen(navController: NavController, viewModel: BioReportViewModel, biomonitorId: String) {
    var selectedTab by remember { mutableStateOf(0) }

    val allReports by viewModel.bioReports.collectAsState()
    val savedReports = allReports.filter { !it.status }
    val uploadedReports = allReports.filter { it.status }

    // Fetch reports on screen load
    LaunchedEffect(Unit) {
        viewModel.fetchReportsForUser(biomonitorId)
    }

    MainLayout(navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Tab Navigation
            TabRow(
                selectedTabIndex = selectedTab,
                backgroundColor = ObjectGreen2,
                contentColor = Color.White
            ) {
                TabItem("Todos", selectedTab == 0) { selectedTab = 0 }
                TabItem("Guardados", selectedTab == 1) { selectedTab = 1 }
                TabItem("Subidos", selectedTab == 2) { selectedTab = 2 }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display content based on selected tab
            when (selectedTab) {
                0 -> ReportList(reports = allReports)          // "Todos" tab
                1 -> ReportList(reports = savedReports)        // "Guardados" tab
                2 -> ReportList(reports = uploadedReports)     // "Subidos" tab
            }
        }
    }
}

@Composable
fun TabItem(title: String, selected: Boolean, onClick: () -> Unit) {
    Tab(
        selected = selected,
        onClick = onClick,
        text = { Text(title, fontWeight = FontWeight.Bold, color = if (selected) Color.White else Color.LightGray) }
    )
}

@Composable
fun ReportList(reports: List<BioReport>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(reports) { report ->
            ReportCard(report)
        }
    }
}

@Composable
fun ReportCard(report: BioReport) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ID: ${report.id}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ObjectGreen1)
            Text("Type: ${report.type}", fontSize = 16.sp)
            Text("Date: ${report.date}", fontSize = 16.sp)
            Text("Status: ${if (report.status) "Uploaded" else "Saved"}", fontSize = 16.sp)
        }
    }
}