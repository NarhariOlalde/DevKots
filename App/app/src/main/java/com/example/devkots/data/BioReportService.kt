package com.example.devkots.data

import com.example.devkots.model.BioReport
import com.example.devkots.model.FaunaTransectoReport
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BioReportService {
    @GET("basic_report")
    suspend fun getAllReports(): Response<List<BioReport>>

    @POST("basic_report") // Endpoint for shared report collection
    suspend fun submitFaunaTransectoReport(@Body report: FaunaTransectoReport): Response<FaunaTransectoReport>
}