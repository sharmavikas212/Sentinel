package com.vikas.sentinel.data.sensor

import kotlinx.coroutines.flow.Flow

interface SensorDataSource {
    fun getSensorData(): Flow<FloatArray>
}