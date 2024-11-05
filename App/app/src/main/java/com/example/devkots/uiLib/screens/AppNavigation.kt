package com.example.devkots.uiLib.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devkots.uiLib.viewmodels.BioReportViewModel
import com.example.devkots.uiLib.viewmodels.LoginViewModel
import com.example.devkots.uiLib.viewmodels.SignupViewModel
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel = UserSessionViewModel(),
    bioReportViewModel: BioReportViewModel = BioReportViewModel()
) {
    NavHost(navController = navController, startDestination = "login_signup") {
        composable("login_signup") { LoginSignupScreen(navController) }
        composable("login") { LoginScreen(navController, LoginViewModel(userSessionViewModel), userSessionViewModel) }
        composable("signup") { SignupScreen(navController, SignupViewModel(), userSessionViewModel) }
        composable("dashboard") {
            DashboardScreen(
                navController = navController,
                viewModel = bioReportViewModel,
                userName = userSessionViewModel.userName.collectAsState().value,
                biomonitorId = userSessionViewModel.biomonitorId.collectAsState().value
            )
        }
        composable("search") {
            SearchScreen(
                navController = navController,
                viewModel = bioReportViewModel,
                biomonitorId = userSessionViewModel.biomonitorId.collectAsState().value
            )
        }
        composable("configuracion") {
            ConfigurationScreen(
                navController = navController,
                userSessionViewModel = userSessionViewModel
            )
        }
        composable("report_selection") { ReportSelectionScreen(navController) }

        // Placeholder routes for report types
        composable("fauna_transect_form/{weather}/{season}") { backStackEntry ->
            val weather = backStackEntry.arguments?.getString("weather")
            val season = backStackEntry.arguments?.getString("season")
            FaunaTransectoFormScreen(
                navController,
                userSessionViewModel.biomonitorId.collectAsState().value,
                weather ?: "Estado del tiempo no disponible",
                season ?: "Temporada no disponible"
            )
        }
        composable("fauna_point_count_form") { PlaceholderFormScreen("Fauna en Punto de Conteo") }
        composable("fauna_free_search_form") { PlaceholderFormScreen("Fauna Busqueda Libre") }
        composable("coverage_validation_form") { PlaceholderFormScreen("Validacion de Cobertura") }
        composable("vegetation_plot_form") { PlaceholderFormScreen("Parcela de Vegetacion") }
        composable("trap_cameras_form") { PlaceholderFormScreen("Camaras Trampa") }
        composable("climatic_variables_form") { PlaceholderFormScreen("Variables Climaticas") }
    }
}

@Composable
fun PlaceholderFormScreen(formType: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Form for $formType", fontSize = 24.sp)
    }
}