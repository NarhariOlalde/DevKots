package com.example.devkots.uiLib.viewmodels

import com.example.devkots.data.BioReportService
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.BioReport
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockApi = mockk()
        mockkObject(RetrofitInstanceBioReport)
        every { RetrofitInstanceBioReport.api } returns mockApi
        viewModel = BioReportViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Descripción: Esta prueba verifica que cuando se obtienen informes válidos para un usuario, el estado del ViewModel se actualiza correctamente con los datos del informe, el total de informes, los recuentos de informes verdaderos/falsos y el porcentaje de informes verdaderos.
    // Escenario: Simula una respuesta exitosa de la API con una lista de objetos BioReport.
    // Aserciones: Comprueba si las variables de estado del ViewModel (como bioReports, totalReports, trueReportsCount, etc.) contienen los valores esperados en función de los informes simulados.
    @Test
    fun fetchReportsForUser_ValidReports_UpdatesStateCorrectly() = runTest(testDispatcher) {
        val biomonitorId = "123"
        val mockReports = listOf(
            BioReport(id = 1, date = "2024-11-12", status = true, biomonitor_id = "123", type = "Type A"),
            BioReport(id = 2, date = "2024-11-12", status = false, biomonitor_id = "123", type = "Type B"),
            BioReport(id = 3, date = "2024-11-12", status = true, biomonitor_id = "123", type = "Type C")
        )
        coEvery { mockApi.getAllReports() } returns Response.success(mockReports)

        viewModel.fetchReportsForUser(biomonitorId)
        advanceUntilIdle() // Wait for coroutine to finish

        val bioReports = viewModel.bioReports.value
        val totalReports = viewModel.totalReports.value
        val trueReportsCount = viewModel.trueReportsCount.value
        val falseReportsCount = viewModel.falseReportsCount.value
        val trueStatusPercentage = viewModel.trueStatusPercentage.value

        assertEquals(3, totalReports)
        assertEquals(2, trueReportsCount)
        assertEquals(1, falseReportsCount)
        assertEquals(66.67, trueStatusPercentage, 0.01) // Percentage calculation
        assertEquals(mockReports, bioReports)
    }

    // Descripción: Esta prueba verifica que cuando la solicitud a la API para obtener informes falla, el ViewModel establece un mensaje de error apropiado y asegura que la lista de informes esté vacía.
    // Escenario: Simula una respuesta de error de la API (error 404).
    // Aserciones: Comprueba si el errorMessage en el ViewModel contiene el mensaje de error esperado y si la lista bioReports está vacía.
    @Test
    fun fetchReportsForUser_ApiFailure_SetsErrorMessage() = runTest(testDispatcher) {
        coEvery { mockApi.getAllReports() } returns Response.error(404, mockk(relaxed = true))
        viewModel.fetchReportsForUser("123")
        advanceUntilIdle() 
        assertEquals("Failed to fetch reports: Response.error()", viewModel.errorMessage.value)
        assertTrue(viewModel.bioReports.value.isEmpty())
    }

    // Descripción: Esta prueba verifica que cuando se lanza una excepción durante la solicitud a la API, el ViewModel establece un mensaje de error apropiado y asegura que la lista de informes esté vacía.
    // Escenario: Simula que se lanza una RuntimeException durante la llamada a la API.
    // Aserciones: Comprueba si el errorMessage en el ViewModel contiene el mensaje de error esperado y si la lista bioReports está vacía.
    @Test
    fun fetchReportsForUser_ExceptionThrown_SetsErrorMessage() = runTest(testDispatcher) {
        coEvery { mockApi.getAllReports() } throws RuntimeException("Network error")
        viewModel.fetchReportsForUser("123")
        advanceUntilIdle()
        assertEquals("Error: Network error", viewModel.errorMessage.value)
        assertTrue(viewModel.bioReports.value.isEmpty())
    }

    // Descripción: Esta prueba verifica que cuando no se encuentran informes para un ID de biomonitor específico, el ViewModel establece un estado vacío, lo que indica que no hay datos disponibles.
    // Escenario: Simula una respuesta exitosa de la API pero con una lista vacía de informes para el ID de biomonitor dado.
    // Aserciones: Comprueba si la lista bioReports está vacía y si las variables de estado relacionadas (como totalReports, trueReportsCount, etc.) están establecidas en 0.
    @Test
    fun fetchReportsForUser_NoReportsForBiomonitorId_SetsEmptyState() = runTest(testDispatcher) {
        val biomonitorId = "456"
        val mockReports = listOf(
            BioReport(id = 1, date = "2024-11-12", status = true, biomonitor_id = "123", type = "Type A")
        )

        coEvery { mockApi.getAllReports() } returns Response.success(mockReports)
        viewModel.fetchReportsForUser(biomonitorId)
        advanceUntilIdle()

        val bioReports = viewModel.bioReports.value
        val totalReports = viewModel.totalReports.value
        val trueReportsCount = viewModel.trueReportsCount.value
        val falseReportsCount = viewModel.falseReportsCount.value
        val trueStatusPercentage = viewModel.trueStatusPercentage.value

        assertTrue(bioReports.isEmpty())
        assertEquals(0, totalReports)
        assertEquals(0, trueReportsCount)
        assertEquals(0, falseReportsCount)
        assertEquals(0.0, trueStatusPercentage, 0.01)
    }
}