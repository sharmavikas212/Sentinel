package com.vikas.sentinel.data.repository

import android.hardware.Sensor
import com.vikas.sentinel.data.room.AppDatabase
import com.vikas.sentinel.data.room.SensorRecord
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
    private val db: AppDatabase
) : SensorRepository {

    // Helper to convert Int constants to readable strings for the DB
    private fun getSensorName(sensorType: Int): String {
        return when (sensorType) {
            Sensor.TYPE_LIGHT -> "LIGHT"
            Sensor.TYPE_PROXIMITY -> "PROXIMITY"
            Sensor.TYPE_PRESSURE -> "BAROMETER"
            Sensor.TYPE_AMBIENT_TEMPERATURE -> "TEMPERATURE"
            Sensor.TYPE_RELATIVE_HUMIDITY -> "HUMIDITY"
            Sensor.TYPE_ACCELEROMETER -> "ACCELEROMETER"
            Sensor.TYPE_GYROSCOPE -> "GYROSCOPE"
            Sensor.TYPE_MAGNETIC_FIELD -> "MAGNETOMETER"
            0 -> "BATTERY" // Placeholder used in your BatterySensor
            else -> "SENSOR_$sensorType"
        }
    }

    private val accelerometerBuffer = mutableListOf<SensorRecord>()

    // Helper for single-value environmental sensors
    private fun MeasurableSensor.asEnvironmentalFlow(
        unitType: SensorUnit,
        sensorType: Int
    ): Flow<EnvironmentalReading> {
        return this.sensorFlow
            .onEach { values ->
                val startTime = System.currentTimeMillis()
                saveSensorReading(
                    sensorType = getSensorName(sensorType),
                    values = values,
                    unit = unitType
                )
                val duration = System.currentTimeMillis() - startTime
                println("Single inserts took: $duration ms")
            }
            .map { values ->
                EnvironmentalReading(
                    value = values.firstOrNull() ?: 0f,
                    unit = unitType
                )
            }
            .flowOn(Dispatchers.IO)

    }

    // Helper for 3-axis vector sensors
    private fun MeasurableSensor.asVectorFlow(
        unitType: SensorUnit,
        sensorType: Int
    ): Flow<VectorReading> {
        return this.sensorFlow
            .onEach { values ->
                val startTime = System.currentTimeMillis()
                saveSensorReading(
                    sensorType = getSensorName(sensorType),
                    values = values,
                    unit = unitType
                )
                val duration = System.currentTimeMillis() - startTime
                println("Single inserts took - vector: $duration ms")
            }
            .map { values ->
                VectorReading(
                    x = values.getOrNull(0) ?: 0f,
                    y = values.getOrNull(1) ?: 0f,
                    z = values.getOrNull(2) ?: 0f
                )
            }
            .flowOn(Dispatchers.IO)

    }

    override fun getLightSensorData(): Flow<EnvironmentalReading> =
        lightSensor.asEnvironmentalFlow(SensorUnit.LUX, Sensor.TYPE_LIGHT)

    override fun getProximitySensorData(): Flow<EnvironmentalReading> =
        proximitySensor.asEnvironmentalFlow(SensorUnit.CM, Sensor.TYPE_PROXIMITY)

    override fun getPressureSensorData(): Flow<EnvironmentalReading> =
        pressureSensor.asEnvironmentalFlow(SensorUnit.HPA, Sensor.TYPE_PRESSURE)

    override fun getAmbientTemperatureSensorData(): Flow<EnvironmentalReading> =
        ambientTemperatureSensor.asEnvironmentalFlow(
            SensorUnit.CELSIUS,
            Sensor.TYPE_AMBIENT_TEMPERATURE
        )

    override fun getHumiditySensorData(): Flow<EnvironmentalReading> =
        humiditySensor.asEnvironmentalFlow(SensorUnit.PERCENT, Sensor.TYPE_RELATIVE_HUMIDITY)

    override fun getBatteryPercentage(): Flow<EnvironmentalReading> =
        batterySensor.asEnvironmentalFlow(SensorUnit.PERCENT, 0)

    override suspend fun saveSensorReading(
        sensorType: String,
        values: FloatArray,
        unit: SensorUnit
    ) {
        db.sensorDao().insertSensorReading(
            SensorRecord(
                sensorType = sensorType,
                values = values,
                unit = unit,
                isSynced = false
            )
        )
    }

    override fun getAccelerometerSensorData(): Flow<VectorReading> =
        accelerometerSensor.asVectorFlow(SensorUnit.M_S2, Sensor.TYPE_ACCELEROMETER)

    override fun getGyroscopeSensorData(): Flow<VectorReading> =
        gyroscopeSensor.asVectorFlow(SensorUnit.RAD_S, Sensor.TYPE_GYROSCOPE)

    override fun getMagnetometerSensorData(): Flow<VectorReading> =
        magnetometerSensor.asVectorFlow(SensorUnit.UT, Sensor.TYPE_MAGNETIC_FIELD)
}