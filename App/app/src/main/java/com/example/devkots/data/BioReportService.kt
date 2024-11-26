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
    suspend fun submitFaunaTransectoReport(@Body report: Any?): Response<FaunaTransectoReport>

    @POST("basic_report")
    suspend fun submitFaunaPuntoConteoReport(@Body report: Any?): Response<FaunaPuntoConteoReport>

    @POST("basic_report")
    suspend fun submitFaunaBusquedaReport(@Body report: Any?): Response<FaunaBusquedaReport>

    @POST("basic_report")
    suspend fun submitValidacionCoberturaReport(@Body report: Any?): Response<ValidacionCoberturaReport>

    @POST("basic_report")
    suspend fun submitParcelaVegetacionReport(@Body report: Any?): Response<ParcelaVegetacionReport>

    @POST("basic_report")
    suspend fun submitCamarasTrampaReport(@Body report: Any?): Response<CamarasTrampaReport>

    @POST("basic_report")
    suspend fun submitVariablesClimaticasReport(@Body report: Any?): Response<VariablesClimaticasReport>

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

}