package com.example.devkots.data

import com.example.devkots.model.BioReport
import com.example.devkots.model.CamarasTrampaReport
import com.example.devkots.model.FaunaBusquedaReport
import com.example.devkots.model.FaunaPuntoConteoReport
import com.example.devkots.model.FaunaTransectoReport
import com.example.devkots.model.ParcelaVegetacionReport
import com.example.devkots.model.ValidacionCoberturaReport
import com.example.devkots.model.VariablesClimaticasReport
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
    suspend fun submitFaunaBusquedaReport(@Body report: FaunaBusquedaReport): Response<FaunaBusquedaReport>

    @POST("basic_report")
    suspend fun submitValidacionCoberturaReport(@Body report: ValidacionCoberturaReport): Response<ValidacionCoberturaReport>

    @POST("basic_report")
    suspend fun submitParcelaVegetacionReport(@Body report: ParcelaVegetacionReport): Response<ParcelaVegetacionReport>

    @POST("basic_report")
    suspend fun submitCamarasTrampaReport(@Body report: CamarasTrampaReport): Response<CamarasTrampaReport>

    @POST("basic_report")
    suspend fun submitVariablesClimaticasReport(@Body report: VariablesClimaticasReport): Response<VariablesClimaticasReport>

    @GET("basic_report/{id}")
    suspend fun getReportById(@Path("id") id: Int): Response<BioReport>

    // Get specific Fauna en Transecto report by ID
    @GET("basic_report/{id}")
    suspend fun getFaunaTransectoReport(@Path("id") id: Int): Response<FaunaTransectoReport>

    @GET("basic_report/{id}")
    suspend fun getFaunaPuntoConteoReport(@Path("id") id: Int): Response<FaunaPuntoConteoReport>

    @GET("basic_report/{id}")
    suspend fun getFaunaBusquedaLibreReport(@Path("id") id: Int): Response<FaunaBusquedaReport>

    @GET("basic_report/{id}")
    suspend fun getValidacionCoberturaReport(@Path("id") id: Int): Response<ValidacionCoberturaReport>

    @GET("basic_report/{id}")
    suspend fun getParcelaVegetacionReport(@Path("id") id: Int): Response<ParcelaVegetacionReport>

    @GET("basic_report/{id}")
    suspend fun getCamarasTrampaReport(@Path("id") id: Int): Response<CamarasTrampaReport>

    @GET("basic_report/{id}")
    suspend fun getVariablesClimaticasReport(@Path("id") id: Int): Response<VariablesClimaticasReport>

    // Update a Fauna en Transecto report
    @PUT("basic_report/{id}")
    suspend fun updateFaunaTransectoReport(@Path("id") id: Int, @Body report: FaunaTransectoReport): Response<FaunaTransectoReport>

    @PUT("basic_report/{id}")
    suspend fun updateFaunaPuntoConteoReport(@Path("id") id: Int, @Body report: FaunaPuntoConteoReport): Response<FaunaPuntoConteoReport>

    @PUT("basic_report/{id}")
    suspend fun updateFaunaBusquedaLibreReport(@Path("id") id: Int, @Body report: FaunaBusquedaReport): Response<FaunaBusquedaReport>

    @PUT("basic_report/{id}")
    suspend fun updateValidacionCoberturaReport(@Path("id") id: Int, @Body report: ValidacionCoberturaReport): Response<ValidacionCoberturaReport>

    @PUT("basic_report/{id}")
    suspend fun updateParcelaVegetacionReport(@Path("id") id: Int, @Body report: ParcelaVegetacionReport): Response<ParcelaVegetacionReport>

    @PUT("basic_report/{id}")
    suspend fun updateCamarasTrampaReport(@Path("id") id: Int, @Body report: CamarasTrampaReport): Response<CamarasTrampaReport>

    @PUT("basic_report/{id}")
    suspend fun updateVariablesClimaticasReport(@Path("id") id: Int, @Body report: VariablesClimaticasReport): Response<VariablesClimaticasReport>

    @PATCH("basic_report/{id}")
    suspend fun updateReportStatusById(
        @Path("id") id: Int,
        @Body statusUpdate: Map<String, Boolean> // Only updating the status field
    ): Response<Unit>
}