package com.vikas.sentinel.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikas.sentinel.data.sensor.MeasurableSensor
import com.vikas.sentinel.hilt.AccelerometerSensorQualifier
import com.vikas.sentinel.hilt.AmbientTemperatureSensorQualifier
import com.vikas.sentinel.hilt.GyroscopeSensorQualifier
import com.vikas.sentinel.hilt.HumiditySensorQualifier
import com.vikas.sentinel.hilt.LightSensorQualifier
import com.vikas.sentinel.hilt.MagnetometerSensorQualifier
import com.vikas.sentinel.hilt.PressureSensorQualifier
import com.vikas.sentinel.hilt.ProximitySensorQualifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class HomeViewModel @Inject constructor(
    @LightSensorQualifier private val lightSensor: MeasurableSensor,
    @ProximitySensorQualifier private val proximitySensor: MeasurableSensor,
    @AccelerometerSensorQualifier private val accelerometerSensor: MeasurableSensor,
    @GyroscopeSensorQualifier private val gyroscopeSensor: MeasurableSensor,
    @PressureSensorQualifier private val pressureSensor: MeasurableSensor,
    @MagnetometerSensorQualifier private val magnetometerSensor: MeasurableSensor,
    @AmbientTemperatureSensorQualifier private val ambientTemperatureSensor: MeasurableSensor,
    @HumiditySensorQualifier private val humiditySensor: MeasurableSensor
) : ViewModel() {

    // Optimization: Define a threshold (e.g., 0.01 change required to update UI)
    private val significantChangeThreshold = 0.01f

    // --- Helper 1: Optimize Single Value Sensors ---
    // 1. Maps array to float.
    // 2. Filters out noise (micro-changes).
    // 3. Converts to StateFlow with lifecycle timeout.
    private fun MeasurableSensor.toStateFlow(initialValue: Float = 0f): StateFlow<Float> {
        return this.sensorFlow
            .map { it.firstOrNull() ?: initialValue }
            .distinctUntilChanged { old, new ->
                abs(old - new) < significantChangeThreshold
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = initialValue
            )
    }

    // --- Helper 2: Optimize Array Sensors ---
    // 1. Checks CONTENT equality (the actual numbers) instead of memory reference.
    // 2. Converts to StateFlow with lifecycle timeout.
    private fun MeasurableSensor.toArrayStateFlow(): StateFlow<FloatArray> {
        return this.sensorFlow
            // CRITICAL OPTIMIZATION:
            // Without this, the UI recomposes on every single sensor event (60Hz+).
            // contentEquals returns true only if the numbers inside are the same.
            .distinctUntilChanged { old, new ->
                // OPTIMIZATION: Check for noise across the entire array.
                // If arrays are different sizes, they are definitely different.
                if (old.size != new.size) return@distinctUntilChanged false

                // Check every value. If ANY value changed significantly, return false (emit update).
                for (i in old.indices) {
                    if (abs(old[i] - new[i]) >= significantChangeThreshold) {
                        return@distinctUntilChanged false
                    }
                }
                // If we get here, all changes were tiny (noise). Return true to drop the update.
                true
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FloatArray(3)
            )
    }

    // --- Exposed States ---

    val lightValue = lightSensor.toStateFlow()
    val proximityValue = proximitySensor.toStateFlow()
    val pressureValue = pressureSensor.toStateFlow()
    val ambientTemperatureValue = ambientTemperatureSensor.toStateFlow()
    val humidityValue = humiditySensor.toStateFlow()

    // Now these are fully optimized:
    val accelerometerValue = accelerometerSensor.toArrayStateFlow()
    val gyroscopeValue = gyroscopeSensor.toArrayStateFlow()
    val magnetometerValue = magnetometerSensor.toArrayStateFlow()
}