package com.vikas.sentinel.domain.model

// For Accelerometer, Gyroscope, Magnetometer
data class VectorReading(
    val x: Float,
    val y: Float,
    val z: Float,
    val timestamp: Long = System.currentTimeMillis()
)