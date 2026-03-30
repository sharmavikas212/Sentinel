package com.vikas.sentinel.data.sensor

import com.vikas.sentinel.domain.model.MeasurableSensor

class FakeSensor : MeasurableSensor(sensorType = 0) {

    override val doesSensorExist: Boolean = true

    override fun startListening() {
        // No-op for fake
    }

    override fun stopListening() {
        // No-op for fake
    }

    /**
     * Helper method to manually emit values to the flow
     */
    fun emitData(values: FloatArray) {
        // Invoke the listener defined in the base MeasurableSensor class
        onSensorValueChanged?.invoke(values)
    }
}