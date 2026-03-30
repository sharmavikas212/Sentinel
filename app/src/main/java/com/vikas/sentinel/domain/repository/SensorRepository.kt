package com.vikas.sentinel.domain.repository

import com.vikas.sentinel.domain.model.EnvironmentalReading
import com.vikas.sentinel.domain.model.VectorReading
import kotlinx.coroutines.flow.Flow

interface SensorRepository {
    fun getLightSensorData(): Flow<EnvironmentalReading>
    fun getProximitySensorData(): Flow<EnvironmentalReading>
    fun getAccelerometerSensorData(): Flow<VectorReading>
    fun getGyroscopeSensorData(): Flow<VectorReading>
    fun getPressureSensorData(): Flow<EnvironmentalReading>
    fun getMagnetometerSensorData(): Flow<VectorReading>
    fun getAmbientTemperatureSensorData(): Flow<EnvironmentalReading>
    fun getHumiditySensorData(): Flow<EnvironmentalReading>
    fun getBatteryPercentage(): Flow<EnvironmentalReading>
}