package com.vikas.sentinel.data.repository

import com.vikas.sentinel.domain.model.EnvironmentalReading
import com.vikas.sentinel.domain.model.MeasurableSensor
import com.vikas.sentinel.domain.model.SensorUnit
import com.vikas.sentinel.domain.model.VectorReading
import com.vikas.sentinel.domain.repository.SensorRepository
import com.vikas.sentinel.hilt.AccelerometerSensorQualifier
import com.vikas.sentinel.hilt.AmbientTemperatureSensorQualifier
import com.vikas.sentinel.hilt.BatteryPercentageSensorQualifier
import com.vikas.sentinel.hilt.GyroscopeSensorQualifier
import com.vikas.sentinel.hilt.HumiditySensorQualifier
import com.vikas.sentinel.hilt.LightSensorQualifier
import com.vikas.sentinel.hilt.MagnetometerSensorQualifier
import com.vikas.sentinel.hilt.PressureSensorQualifier
import com.vikas.sentinel.hilt.ProximitySensorQualifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(
    @LightSensorQualifier private val lightSensor: MeasurableSensor,
    @ProximitySensorQualifier private val proximitySensor: MeasurableSensor,
    @AccelerometerSensorQualifier private val accelerometerSensor: MeasurableSensor,
    @GyroscopeSensorQualifier private val gyroscopeSensor: MeasurableSensor,
    @PressureSensorQualifier private val pressureSensor: MeasurableSensor,
    @MagnetometerSensorQualifier private val magnetometerSensor: MeasurableSensor,
    @AmbientTemperatureSensorQualifier private val ambientTemperatureSensor: MeasurableSensor,
    @HumiditySensorQualifier private val humiditySensor: MeasurableSensor,
    @BatteryPercentageSensorQualifier private val batterySensor: MeasurableSensor,
) : SensorRepository {

    // Helper for single-value environmental sensors
    private fun MeasurableSensor.asEnvironmentalFlow(unitType: SensorUnit): Flow<EnvironmentalReading> {
        return this.sensorFlow.map { values ->
            EnvironmentalReading(value = values.firstOrNull() ?: 0f,
                unit = unitType)
        }
    }

    // Helper for 3-axis vector sensors
    private fun MeasurableSensor.asVectorFlow(): Flow<VectorReading> {
        return this.sensorFlow.map { values ->
            VectorReading(
                x = values.getOrNull(0) ?: 0f,
                y = values.getOrNull(1) ?: 0f,
                z = values.getOrNull(2) ?: 0f
            )
        }
    }

    override fun getLightSensorData(): Flow<EnvironmentalReading> = lightSensor.asEnvironmentalFlow(SensorUnit.LUX)

    override fun getProximitySensorData(): Flow<EnvironmentalReading> = proximitySensor.asEnvironmentalFlow(SensorUnit.CM)

    override fun getPressureSensorData(): Flow<EnvironmentalReading> = pressureSensor.asEnvironmentalFlow(SensorUnit.HPA)

    override fun getAmbientTemperatureSensorData(): Flow<EnvironmentalReading> = ambientTemperatureSensor.asEnvironmentalFlow(SensorUnit.CELSIUS)

    override fun getHumiditySensorData(): Flow<EnvironmentalReading> = humiditySensor.asEnvironmentalFlow(SensorUnit.PERCENT)

    override fun getBatteryPercentage(): Flow<EnvironmentalReading> = batterySensor.asEnvironmentalFlow(SensorUnit.PERCENT)

    override fun getAccelerometerSensorData(): Flow<VectorReading> = accelerometerSensor.asVectorFlow()

    override fun getGyroscopeSensorData(): Flow<VectorReading> = gyroscopeSensor.asVectorFlow()

    override fun getMagnetometerSensorData(): Flow<VectorReading> = magnetometerSensor.asVectorFlow()
}