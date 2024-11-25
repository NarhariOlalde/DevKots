package com.example.devkots.uiLib.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devkots.data.AppDatabase
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.components.ReportRepository
import com.example.devkots.uiLib.screens.DetailScreen.ReportCamarasTrampaDetailScreen
import com.example.devkots.uiLib.screens.DetailScreen.ReportFaunaBusquedaLibreDetailScreen
import com.example.devkots.uiLib.screens.DetailScreen.ReportFaunaPuntoConteoDetailScreen
import com.example.devkots.uiLib.screens.DetailScreen.ReportFaunaTransectoDetailScreen
import com.example.devkots.uiLib.screens.DetailScreen.ReportParcelaVegetacionDetailScreen
import com.example.devkots.uiLib.screens.DetailScreen.ReportValidacionCoberturaDetailScreen
import com.example.devkots.uiLib.screens.DetailScreen.ReportVariablesClimaticasDetailScreen
import com.example.devkots.uiLib.screens.FormScreen.CamarasTrampaFormScreen
import com.example.devkots.uiLib.screens.FormScreen.FaunaBusquedaLibreFormScreen
import com.example.devkots.uiLib.screens.FormScreen.FaunaPuntoConteoFormScreen
import com.example.devkots.uiLib.screens.FormScreen.FaunaTransectoFormScreen
import com.example.devkots.uiLib.screens.FormScreen.ParcelaVegetacionFormScreen
import com.example.devkots.uiLib.screens.FormScreen.ValidacionCoberturaFormScreen
import com.example.devkots.uiLib.screens.FormScreen.VariablesClimaticasFormScreen
import com.example.devkots.uiLib.viewmodels.BioReportViewModel
import com.example.devkots.uiLib.viewmodels.LoginViewModel
import com.example.devkots.uiLib.viewmodels.SignupViewModel
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel = UserSessionViewModel(),
) {

    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val reportRepository = remember {
        ReportRepository(
            bioReportDao = database.bioReportDao(),
            faunaTransectoDao = database.faunaTransectoReportDao(),
            faunaPuntoConteoDao = database.faunaPuntoConteoReportDao(),
            faunaBusquedaDao = database.faunaBusquedaReportDao(),
            validacionCoberturaDao = database.validacionCoberturaReportDao(),
            parcelaVegetacionDao = database.parcelaVegetacionReportDao(),
            camarasTrampaDao = database.camarasTrampaReportDao(),
            variablesClimaticasDao = database.variablesClimaticasReportDao()
        )
    }
    val bioReportViewModel = BioReportViewModel(reportRepository)

    val bioReportService = RetrofitInstanceBioReport.api

    NavHost(navController = navController, startDestination = "login_signup") {
        composable("login_signup") { LoginSignupScreen(navController) }
        composable("login") { LoginScreen(navController, LoginViewModel(userSessionViewModel), userSessionViewModel) }
        composable("signup") { SignupScreen( navController, SignupViewModel(userSessionViewModel) ) }
        composable("dashboard") {
            DashboardScreen(
                navController = navController,
                viewModel = bioReportViewModel,
                userName = userSessionViewModel.userName.collectAsState().value,
                biomonitorId = userSessionViewModel.biomonitorId.collectAsState().value,
                userProfilePicture = userSessionViewModel.imageBase64.collectAsState().value
            )
        }

        composable("search") {
            MainLayout(navController = navController) {
                SearchScreen(
                    navController = navController,
                    bioReportService = bioReportService,
                    biomonitorId = userSessionViewModel.biomonitorId.collectAsState().value,
                    reportRepository = reportRepository
                )
            }
        }
        composable("report_detail/{reportId}/{reportType}/{status}") { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId")?.toIntOrNull()
            val reportType = backStackEntry.arguments?.getString("reportType")
            val status = backStackEntry.arguments?.getString("status")?.toBoolean() ?: false
            if (reportId != null && reportType != null) {
                when (reportType) {
                    "Fauna en Transecto" ->
                        ReportFaunaTransectoDetailScreen(
                        navController = navController,
                        status = status,
                        reportId = reportId,
                        bioReportService = bioReportService
                    )

                    "Fauna en Punto de Conteo" -> ReportFaunaPuntoConteoDetailScreen(
                        navController = navController,
                        status = status,
                        reportId = reportId,
                        bioReportService = bioReportService
                    )
                    "Fauna Búsqueda Libre" -> ReportFaunaBusquedaLibreDetailScreen(
                        navController = navController,
                        status = status,
                        reportId = reportId,
                        bioReportService = bioReportService
                    )
                    "Validación de Cobertura" -> ReportValidacionCoberturaDetailScreen(
                        navController = navController,
                        status = status,
                        reportId = reportId,
                        bioReportService = bioReportService
                    )
                    "Parcela de Vegetación" -> ReportParcelaVegetacionDetailScreen(
                        navController = navController,
                        status = status,
                        reportId = reportId,
                        bioReportService = bioReportService
                    )
                    "Cámaras Trampa" -> ReportCamarasTrampaDetailScreen(
                        navController = navController,
                        status = status,
                        reportId = reportId,
                        bioReportService = bioReportService
                    )
                    "Variables Climáticas" -> ReportVariablesClimaticasDetailScreen(
                        navController = navController,
                        status = status,
                        reportId = reportId,
                        bioReportService = bioReportService
                    )
                }
            }
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
        composable("fauna_point_count_form/{weather}/{season}") { backStackEntry ->
            val weather = backStackEntry.arguments?.getString("weather")
            val season = backStackEntry.arguments?.getString("season")
            FaunaPuntoConteoFormScreen(
                navController,
                userSessionViewModel.biomonitorId.collectAsState().value,
                weather ?: "Estado del tiempo no disponible",
                season ?: "Temporada no disponible"
            )
        }
        composable("fauna_free_search_form/{weather}/{season}") { backStackEntry ->
            val weather = backStackEntry.arguments?.getString("weather")
            val season = backStackEntry.arguments?.getString("season")
            FaunaBusquedaLibreFormScreen(
                navController,
                userSessionViewModel.biomonitorId.collectAsState().value,
                weather ?: "Estado del tiempo no disponible",
                season ?: "Temporada no disponible"
            )
        }
        composable("coverage_validation_form/{weather}/{season}") { backStackEntry ->
            val weather = backStackEntry.arguments?.getString("weather")
            val season = backStackEntry.arguments?.getString("season")
            ValidacionCoberturaFormScreen(
                navController,
                userSessionViewModel.biomonitorId.collectAsState().value,
                weather ?: "Estado del tiempo no disponible",
                season ?: "Temporada no disponible"
            )  }
        composable("vegetation_plot_form/{weather}/{season}") { backStackEntry ->
            val weather = backStackEntry.arguments?.getString("weather")
            val season = backStackEntry.arguments?.getString("season")
            ParcelaVegetacionFormScreen(
                navController,
                userSessionViewModel.biomonitorId.collectAsState().value,
                weather ?: "Estado del tiempo no disponible",
                season ?: "Temporada no disponible"
            ) }
        composable("trap_cameras_form/{weather}/{season}") { backStackEntry ->
            val weather = backStackEntry.arguments?.getString("weather")
            val season = backStackEntry.arguments?.getString("season")
            CamarasTrampaFormScreen(
                navController,
                userSessionViewModel.biomonitorId.collectAsState().value,
                weather ?: "Estado del tiempo no disponible",
                season ?: "Temporada no disponible"
            ) }
        composable("climatic_variables_form/{weather}/{season}") { backStackEntry ->
            val weather = backStackEntry.arguments?.getString("weather")
            val season = backStackEntry.arguments?.getString("season")
            VariablesClimaticasFormScreen(
                navController,
                userSessionViewModel.biomonitorId.collectAsState().value,
                weather ?: "Estado del tiempo no disponible",
                season ?: "Temporada no disponible"
            ) }
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