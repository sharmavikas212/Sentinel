package com.vikas.sentinel.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikas.sentinel.domain.model.EnvironmentalReading
import com.vikas.sentinel.domain.model.MeasurableSensor
import com.vikas.sentinel.domain.model.SensorUnit
import com.vikas.sentinel.domain.model.VectorReading
import com.vikas.sentinel.domain.repository.SensorRepository
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
    sensorRepository: SensorRepository
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

    val lightValue: StateFlow<EnvironmentalReading> = sensorRepository.getLightSensorData().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EnvironmentalReading(0f, SensorUnit.LUX)
        )
    val proximityValue = sensorRepository.getProximitySensorData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EnvironmentalReading(0f, SensorUnit.CM)
    )

    val pressureValue = sensorRepository.getPressureSensorData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EnvironmentalReading(0f, SensorUnit.HPA)
    )
    val ambientTemperatureValue = sensorRepository.getAmbientTemperatureSensorData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EnvironmentalReading(0f, SensorUnit.CELSIUS)
    )
    val humidityValue = sensorRepository.getHumiditySensorData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EnvironmentalReading(0f, SensorUnit.PERCENT)
    )

    val batteryPercentage = sensorRepository.getBatteryPercentage().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EnvironmentalReading(0f, SensorUnit.PERCENT)
    )


    // Now these are fully optimized:
    val accelerometerValue = sensorRepository.getAccelerometerSensorData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VectorReading(0f, 0f,0f)
    )
    val gyroscopeValue = sensorRepository.getGyroscopeSensorData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VectorReading(0f, 0f,0f)
    )
    val magnetometerValue = sensorRepository.getMagnetometerSensorData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VectorReading(0f, 0f,0f)
    )
}