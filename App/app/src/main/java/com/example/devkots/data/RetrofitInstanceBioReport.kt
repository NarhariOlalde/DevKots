package com.example.devkots.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceBioReport {
    private const val BASE_URL = "https://retoolapi.dev/AH8DpX/"

    val api: BioReportService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BioReportService::class.java)
    }
}