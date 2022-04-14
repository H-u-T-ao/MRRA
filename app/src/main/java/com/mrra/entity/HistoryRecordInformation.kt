package com.mrra.entity

data class HistoryRecordInformation(
    val deviceInformation: DeviceInformation,
    val operationMode: String,
    val startTime: String,
    val time: String
)