package com.vikas.sentinel.data.sensor

import android.os.Handler
import android.os.HandlerThread
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

abstract class MeasurableSensor(
    protected val sensorType: Int
) {
    protected var onSensorValueChanged: ((FloatArray) -> Unit)? = null

    abstract val doesSensorExist: Boolean

    // Optimization: Background Handler
    protected var sensorThread: HandlerThread? = null
    protected var sensorHandler: Handler? = null

    abstract fun startListening()
    abstract fun stopListening()

    fun setOnSensorValueChangedListener(listener: (FloatArray) -> Unit) {
        onSensorValueChanged = listener
    }

    protected fun initSensorThread() {
        if (sensorThread == null) {
            sensorThread = HandlerThread("SensorThread").apply { start() }
            sensorHandler = Handler(sensorThread!!.looper)
        }
    }

    protected fun cleanupSensorThread() {
        sensorThread?.quitSafely()
        sensorThread = null
        sensorHandler = null
    }

    /**
     * Modern Approach: Expose data as a Flow.
     * 1. callbackFlow converts the callback-based API to a Flow.
     * 2. awaitClose ensures stopListening() is called when the collector stops.
     * 3. conflate() ensures we only process the LATEST value, dropping old ones if the UI is busy.
     */
    val sensorFlow: Flow<FloatArray>
        get() = callbackFlow {
            val listener: (FloatArray) -> Unit = { values ->
                trySend(values)
            }
            setOnSensorValueChangedListener(listener)
            startListening()

            awaitClose {
                stopListening()
            }
        }.conflate()
}