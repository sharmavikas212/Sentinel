package com.vikas.sentinel.domain.model

// For Light, Proximity, Pressure, Humidity, Temp

data class EnvironmentalReading(
    val value: Float,
    val unit: SensorUnit,
    val timestamp: Long = System.currentTimeMillis()
)