package com.example.devkots.uiLib.viewmodels

import android.util.Log
import com.example.devkots.data.BioReportService
import com.example.devkots.data.daos.FaunaTransectoReportDao
import com.example.devkots.model.FaunaTransectoReport
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity
import com.example.devkots.uiLib.components.toFaunaTransectoReport
import com.example.devkots.uiLib.viewmodels.Report.ReportFaunaTransectoViewModel
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mock
import org.mockito.Mockito.`when`
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.mockito.ArgumentMatchers.any
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ReportDetailScreenViewModelTest {

    @Mock
    lateinit var bioReportService: BioReportService
    lateinit var faunaTransectoReportDao: FaunaTransectoReportDao
    private lateinit var viewModel: ReportFaunaTransectoViewModel

    // Se crea el TestCoroutineDispatcher para pruebas
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Configura el Main dispatcher para las pruebas
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0 // Evitar la ejecución del Log

        // Establecer el dispatcher principal para pruebas
        Dispatchers.setMain(testDispatcher)

        // Inicializa el ViewModel
        viewModel = ReportFaunaTransectoViewModel(bioReportService)
    }

    // Prueba que verifica si un reporte se carga con exito desde la fuente remota.
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadReportSuccessful() = runTest {
        val mockReport = FaunaTransectoReport(
            transectoNumber = 1,
            animalType = "Mamífero",
            commonName = "León",
            scientificName = "Panthera leo",
            individualCount = 5,
            observationType = "La Vió",
            weather = "Soleado",
            season = "Verano-Seco",
            observations = "Observación",
            photoPaths = listOf("20041106_125434"),
            date = "2004-11-06",
            time = "12:54:34",
            gpsLocation = "41.40338, 2.17403",
            status = true,
            biomonitor_id = "213"
        )
        val mockResponse = Response.success(mockReport)

        `when`(bioReportService.getFaunaTransectoReport(120)).thenReturn(mockResponse)

        viewModel.loadReport(120, true)

        advanceUntilIdle()

        assertEquals(mockReport, viewModel.report)
        assertFalse(viewModel.loading)
        assertFalse(viewModel.isEditable)
    }

    // Prueba que verifica si un reporte se puede actualizar con exito
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateReportSuccessful() = runTest {
        val mockReport = FaunaTransectoReport(
            transectoNumber = 1,
            animalType = "Mamífero",
            commonName = "León",
            scientificName = "Panthera leo",
            individualCount = 5,
            observationType = "La Vió",
            weather = "Soleado",
            season = "Verano-Seco",
            observations = "Observación",
            photoPaths = listOf("20041106_125434"),
            date = "2004-11-06",
            time = "12:54:34",
            gpsLocation = "41.40338, 2.17403",
            status = true,
            biomonitor_id = "213"
        )

        viewModel.updateReport(2, mockReport)

        advanceUntilIdle()

        assertEquals(mockReport, viewModel.report)
        assertFalse(viewModel.loading)
        assertFalse(viewModel.isEditable)
    }



    @After
    fun tearDown() {
        // Restablece el dispatcher después de la prueba
        Dispatchers.resetMain()

        // Limpiar mockk de Log
        unmockkStatic(Log::class)
    }
}
