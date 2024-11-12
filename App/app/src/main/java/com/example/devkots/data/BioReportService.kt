package com.example.devkots.data

import com.example.devkots.model.BioReport
import com.example.devkots.model.FaunaPuntoConteoReport
import com.example.devkots.model.FaunaTransectoReport
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BioReportService {
    @GET("basic_report")
    suspend fun getAllReports(): Response<List<BioReport>>

    @GET("basic_report")
    suspend fun getAllReportsBiomonitorID(@Query("biomonitor_id") biomonitorId: String): Response<List<BioReport>>

    @POST("basic_report") // Endpoint for shared report collection
    suspend fun submitFaunaTransectoReport(@Body report: FaunaTransectoReport): Response<FaunaTransectoReport>

    @POST("basic_report")
    suspend fun submitFaunaPuntoConteoReport(@Body report: FaunaPuntoConteoReport): Response<FaunaPuntoConteoReport>

    @POST("basic_report")
    suspend fun submitFaunaBusquedaReport(@Body report: FaunaPuntoConteoReport): Response<FaunaPuntoConteoReport>

    @POST("basic_report")
    suspend fun submitValidacionCoberturaReport(@Body report: FaunaPuntoConteoReport): Response<FaunaPuntoConteoReport>

    @POST("basic_report")
    suspend fun submitParcelaVegetacionReport(@Body report: FaunaPuntoConteoReport): Response<FaunaPuntoConteoReport>

    @POST("basic_report")
    suspend fun submitCamarasTrampaReport(@Body report: FaunaPuntoConteoReport): Response<FaunaPuntoConteoReport>

    @POST("basic_report")
    suspend fun submitVariablesClimaticasReport(@Body report: FaunaPuntoConteoReport): Response<FaunaPuntoConteoReport>

    @GET("basic_report/{id}")
    suspend fun getReportById(@Path("id") id: Int): Response<BioReport>

    // Get specific Fauna en Transecto report by ID
    @GET("basic_report/{id}")
    suspend fun getFaunaTransectoReport(@Path("id") id: Int): Response<FaunaTransectoReport>

    // Update a Fauna en Transecto report
    @PUT("basic_report/{id}")
    suspend fun updateFaunaTransectoReport(@Path("id") id: Int, @Body report: FaunaTransectoReport): Response<FaunaTransectoReport>

    @PATCH("basic_report/{id}")
    suspend fun updateReportStatusById(
        @Path("id") id: Int,
        @Body statusUpdate: Map<String, Boolean> // Only updating the status field
    ): Response<Unit>
}