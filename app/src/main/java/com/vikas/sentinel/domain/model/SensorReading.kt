package com.vikas.sentinel.domain.model

enum class SensorUnit {
    LUX, HPA, PERCENT, CELSIUS, CM, UNKNOWN; // Add all relevant units
}
// For Light, Proximity, Pressure, Humidity, Temp
data class EnvironmentalReading(
    val value: Float,
    val unit: SensorUnit
)

// For Accelerometer, Gyroscope, Magnetometer
data class VectorReading(
    val x: Float,
    val y: Float,
    val z: Float
)