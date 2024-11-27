package com.example.devkots.uiLib.viewmodels

import com.example.devkots.data.BioReportService
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.BioReport
import com.example.devkots.model.BioReportEntity
import com.example.devkots.uiLib.components.ReportRepository
import com.example.devkots.uiLib.components.toBioReport
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class BioReportViewModelTest {

    private lateinit var viewModel: BioReportViewModel
    private lateinit var mockApi: BioReportService
    private lateinit var mockRepository: ReportRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockApi = mockk()
        mockRepository = mockk()
        mockkObject(RetrofitInstanceBioReport)
        every { RetrofitInstanceBioReport.api } returns mockApi
        viewModel = BioReportViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Descripción: Verifica que cuando se obtienen reportes válidos desde fuentes remotas y locales,
    // el estado del ViewModel se actualiza correctamente con los datos combinados.
    // Escenario: Simula una respuesta exitosa de la API y datos locales relevantes del repositorio.
    // Aserciones: Comprueba si las variables del estado (bioReports, totalReports, trueReportsCount, etc.)
    // se actualizan correctamente con los datos combinados de las fuentes.
    @Test
    fun fetchReportsForUser_ValidReports_UpdatesStateCorrectly() = runTest(testDispatcher) {
        val biomonitorId = "123"
        val remoteReports = listOf(
            BioReport(id = 1, date = "2024-11-12", status = true, biomonitor_id = biomonitorId, type = "Type A"),
            BioReport(id = 2, date = "2024-11-12", status = false, biomonitor_id = biomonitorId, type = "Type B")
        )
        val localReports = listOf(
            BioReportEntity(
                id = 3, formId = 3L, date = "2024-11-12",
                status = true, biomonitor_id = biomonitorId, type = "Type C"
            )
        )

        coEvery { mockApi.getAllReports() } returns Response.success(remoteReports)
        coEvery { mockRepository.getAllLocalReports() } returns localReports

        viewModel.fetchReportsForUser(biomonitorId)
        advanceUntilIdle()

        val bioReports = viewModel.bioReports.first()
        val totalReports = viewModel.totalReports.first()
        val trueReportsCount = viewModel.trueReportsCount.first()
        val falseReportsCount = viewModel.falseReportsCount.first()
        val trueStatusPercentage = viewModel.trueStatusPercentage.first()

        assertEquals(3, totalReports)
        assertEquals(2, trueReportsCount)
        assertEquals(1, falseReportsCount)
        assertEquals(66.67, trueStatusPercentage, 0.01) // Porcentaje calculado
        assertEquals(remoteReports + localReports.map { it.toBioReport() }, bioReports)
    }

    // Descripción: Verifica que cuando la solicitud a la API falla, el ViewModel establece
    // un mensaje de error y asegura que la lista de reportes está vacía.
    // Escenario: Simula una respuesta de error de la API (error 404).
    // Aserciones: Comprueba que el errorMessage contiene el mensaje esperado y que bioReports está vacío.
    @Test
    fun fetchReportsForUser_ApiFailure_SetsErrorMessage() = runTest(testDispatcher) {
        coEvery { mockApi.getAllReports() } returns Response.error(404, mockk(relaxed = true))
        coEvery { mockRepository.getAllLocalReports() } returns emptyList()

        viewModel.fetchReportsForUser("123")
        advanceUntilIdle()

        assertEquals("Failed to fetch remote reports: Response.error()", viewModel.errorMessage.first())
        assertTrue(viewModel.bioReports.first().isEmpty())
    }

    // Descripción: Verifica que cuando ocurre una excepción durante la solicitud a la API,
    // el ViewModel establece un mensaje de error apropiado y asegura que la lista de reportes está vacía.
    // Escenario: Simula que una RuntimeException es lanzada durante la llamada a la API.
    // Aserciones: Comprueba que el errorMessage contiene el mensaje de la excepción y que bioReports está vacío.
    @Test
    fun fetchReportsForUser_ExceptionThrown_SetsErrorMessage() = runTest(testDispatcher) {
        coEvery { mockApi.getAllReports() } throws RuntimeException("Network error")
        coEvery { mockRepository.getAllLocalReports() } returns emptyList()

        viewModel.fetchReportsForUser("123")
        advanceUntilIdle()

        assertEquals("Error: Network error", viewModel.errorMessage.first())
        assertTrue(viewModel.bioReports.first().isEmpty())
    }

    // Descripción: Verifica que cuando no hay reportes para un biomonitor específico en las fuentes
    // remotas y locales, el estado del ViewModel refleja correctamente la ausencia de datos.
    // Escenario: Simula una respuesta exitosa de la API y datos locales, pero ninguno
    // pertenece al biomonitor especificado.
    // Aserciones: Comprueba que bioReports está vacío y que las métricas relacionadas están en 0.
    @Test
    fun fetchReportsForUser_NoReportsForBiomonitorId_SetsEmptyState() = runTest(testDispatcher) {
        val biomonitorId = "456"
        val remoteReports = listOf(
            BioReport(id = 1, date = "2024-11-12", status = true, biomonitor_id = "123", type = "Type A")
        )
        val localReports = listOf(
            BioReportEntity(
                id = 2, formId = 2L, date = "2024-11-12",
                status = false, biomonitor_id = "123", type = "Type B"
            )
        )

        coEvery { mockApi.getAllReports() } returns Response.success(remoteReports)
        coEvery { mockRepository.getAllLocalReports() } returns localReports

        viewModel.fetchReportsForUser(biomonitorId)
        advanceUntilIdle()

        val bioReports = viewModel.bioReports.first()
        val totalReports = viewModel.totalReports.first()
        val trueReportsCount = viewModel.trueReportsCount.first()
        val falseReportsCount = viewModel.falseReportsCount.first()
        val trueStatusPercentage = viewModel.trueStatusPercentage.first()

        assertTrue(bioReports.isEmpty())
        assertEquals(0, totalReports)
        assertEquals(0, trueReportsCount)
        assertEquals(0, falseReportsCount)
        assertEquals(0.0, trueStatusPercentage, 0.01)
    }
}