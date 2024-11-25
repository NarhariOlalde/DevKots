package com.example.devkots.uiLib.components

import com.example.devkots.model.BioReport
import com.example.devkots.model.BioReportEntity
import com.example.devkots.model.CamarasTrampaReport
import com.example.devkots.model.FaunaBusquedaReport
import com.example.devkots.model.FaunaPuntoConteoReport
import com.example.devkots.model.FaunaTransectoReport
import com.example.devkots.model.LocalEntities.CamarasTrampaReportEntity
import com.example.devkots.model.LocalEntities.FaunaBusquedaReportEntity
import com.example.devkots.model.LocalEntities.FaunaPuntoConteoReportEntity
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity
import com.example.devkots.model.LocalEntities.ParcelaVegetacionReportEntity
import com.example.devkots.model.LocalEntities.ValidacionCoberturaReportEntity
import com.example.devkots.model.LocalEntities.VariablesClimaticasReportEntity
import com.example.devkots.model.ParcelaVegetacionReport
import com.example.devkots.model.ValidacionCoberturaReport
import com.example.devkots.model.VariablesClimaticasReport

fun FaunaTransectoReportEntity.toFaunaTransectoReport(): FaunaTransectoReport {
    return FaunaTransectoReport(
        type = this.type,
        transectoNumber = this.transectoNumber,
        animalType = this.animalType,
        commonName = this.commonName,
        scientificName = this.scientificName,
        individualCount = this.individualCount,
        observationType = this.observationType,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun FaunaPuntoConteoReportEntity.toFaunaPuntoConteoReport(): FaunaPuntoConteoReport {
    return FaunaPuntoConteoReport(
        type = this.type,
        zone = this.zone,
        animalType = this.animalType,
        commonName = this.commonName,
        scientificName = this.scientificName,
        individualCount = this.individualCount,
        observationType = this.observationType,
        observationHeight = this.observationHeight,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun FaunaBusquedaReportEntity.toFaunaBusquedaReport(): FaunaBusquedaReport {
    return FaunaBusquedaReport(
        type = this.type,
        zone = this.zone,
        animalType = this.animalType,
        commonName = this.commonName,
        scientificName = this.scientificName,
        individualCount = this.individualCount,
        observationType = this.observationType,
        observationHeight = this.observationHeight,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun ValidacionCoberturaReportEntity.toValidacionCoberturaReport(): ValidacionCoberturaReport {
    return ValidacionCoberturaReport(
        type = this.type,
        code = this.code,
        seguimiento = this.seguimiento,
        cambio = this.cambio,
        cobertura = this.cobertura,
        tiposCultivo = this.tiposCultivo,
        disturbio = this.disturbio,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun ParcelaVegetacionReportEntity.toParcelaVegetacionReport(): ParcelaVegetacionReport {
    return ParcelaVegetacionReport(
        type = this.type,
        code = this.code,
        cuadrante = this.cuadrante,
        subcuadrante = this.subcuadrante,
        habitocrecimiento = this.habitocrecimiento,
        nombrecomun = this.nombrecomun,
        nombrecientifico = this.nombrecientifico,
        placa = this.placa,
        circunferencia = this.circunferencia,
        distancia = this.distancia,
        estaturabio = this.estaturabio,
        altura = this.altura,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun CamarasTrampaReportEntity.toCamarasTrampaReport(): CamarasTrampaReport {
    return CamarasTrampaReport(
        type = this.type,
        code = this.code,
        zona = this.zona,
        nombrecamara = this.nombrecamara,
        placacamara = this.placacamara,
        placaguaya = this.placaguaya,
        anchocamino = this.anchocamino,
        fechainstalacion = this.fechainstalacion,
        distancia = this.distancia,
        altura = this.altura,
        listachequeo = this.listachequeo,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun VariablesClimaticasReportEntity.toVariablesClimaticasReport(): VariablesClimaticasReport {
    return VariablesClimaticasReport(
        type = this.type,
        zona = this.zona,
        pluviosidad = this.pluviosidad,
        tempmax = this.tempmax,
        humedadmax = this.humedadmax,
        tempmin = this.tempmin,
        nivelquebrada = this.nivelquebrada,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun BioReportEntity.toBioReport(): BioReport {
    return BioReport(
        id = this.id,
        type = this.type,
        date = this.date,
        status = this.status,
        biomonitor_id = this.biomonitor_id,
    )
}

fun FaunaTransectoReport.toEntity(reportId: Int): FaunaTransectoReportEntity {
    return FaunaTransectoReportEntity(
        id = reportId,
        type = this.type,
        transectoNumber = this.transectoNumber,
        animalType = this.animalType,
        commonName = this.commonName,
        scientificName = this.scientificName,
        individualCount = this.individualCount,
        observationType = this.observationType,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun FaunaPuntoConteoReport.toEntity(reportId: Int): FaunaPuntoConteoReportEntity {
    return FaunaPuntoConteoReportEntity(
        id = reportId,
        type = this.type,
        zone = this.zone,
        animalType = this.animalType,
        commonName = this.commonName,
        scientificName = this.scientificName,
        individualCount = this.individualCount,
        observationType = this.observationType,
        observationHeight = this.observationHeight,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun FaunaBusquedaReport.toEntity(reportId: Int): FaunaBusquedaReportEntity {
    return FaunaBusquedaReportEntity(
        id = reportId,
        type = this.type,
        zone = this.zone,
        animalType = this.animalType,
        commonName = this.commonName,
        scientificName = this.scientificName,
        individualCount = this.individualCount,
        observationType = this.observationType,
        observationHeight = this.observationHeight,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun ValidacionCoberturaReport.toEntity(reportId: Int): ValidacionCoberturaReportEntity {
    return ValidacionCoberturaReportEntity(
        id = reportId,
        type = this.type,
        code = this.code,
        seguimiento = this.seguimiento,
        cambio = this.cambio,
        cobertura = this.cobertura,
        tiposCultivo = this.tiposCultivo,
        disturbio = this.disturbio,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun ParcelaVegetacionReport.toEntity(reportId: Int): ParcelaVegetacionReportEntity {
    return ParcelaVegetacionReportEntity(
        id = reportId,
        type = this.type,
        code = this.code,
        cuadrante = this.cuadrante,
        subcuadrante = this.subcuadrante,
        habitocrecimiento = this.habitocrecimiento,
        nombrecomun = this.nombrecomun,
        nombrecientifico = this.nombrecientifico,
        placa = this.placa,
        circunferencia = this.circunferencia,
        distancia = this.distancia,
        estaturabio = this.estaturabio,
        altura = this.altura,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun CamarasTrampaReport.toEntity(reportId: Int): CamarasTrampaReportEntity {
    return CamarasTrampaReportEntity(
        id = reportId,
        type = this.type,
        code = this.code,
        zona = this.zona,
        nombrecamara = this.nombrecamara,
        placacamara = this.placacamara,
        placaguaya = this.placaguaya,
        anchocamino = this.anchocamino,
        fechainstalacion = this.fechainstalacion,
        distancia = this.distancia,
        altura = this.altura,
        listachequeo = this.listachequeo,
        photoPaths = this.photoPaths,
        observations = this.observations,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}

fun VariablesClimaticasReport.toEntity(reportId: Int): VariablesClimaticasReportEntity {
    return VariablesClimaticasReportEntity(
        id = reportId,
        type = this.type,
        zona = this.zona,
        pluviosidad = this.pluviosidad,
        tempmax = this.tempmax,
        humedadmax = this.humedadmax,
        tempmin = this.tempmin,
        nivelquebrada = this.nivelquebrada,
        date = this.date,
        time = this.time,
        gpsLocation = this.gpsLocation,
        weather = this.weather,
        status = true,
        biomonitor_id = this.biomonitor_id,
        season = this.season
    )
}


