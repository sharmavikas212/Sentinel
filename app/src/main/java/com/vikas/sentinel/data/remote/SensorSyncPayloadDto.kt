package com.vikas.sentinel.data.remote

/**
 * Payload for cloud sync. Batching reduces battery/data usage.
 */
data class SensorSyncPayloadDto(
    val deviceId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val readings: List<SensorReadingDto>
)

/**
 * Generic reading DTO. Handles both Environmental (value)
 * and Vector (x, y, z) data.
 */
data class SensorReadingDto(
    val sensorType: String, // e.g., "LIGHT", "ACCELEROMETER"
    val timestamp: Long,
    val unit: String,
    val value: Float? = null,
    val x: Float? = null,
    val y: Float? = null,
    val z: Float? = null
)
