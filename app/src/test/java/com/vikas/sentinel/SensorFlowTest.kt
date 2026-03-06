package com.vikas.sentinel

import com.vikas.sentinel.data.sensor.FakeSensor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SensorFlowTest {

    @Test
    fun `sensor flow emits values when data is received`() = runTest {
        // 1. Create the fake sensor
        val fakeSensor = FakeSensor()
        val expectedValues = floatArrayOf(10f, 20f, 30f)

        // 2. Collect the flow in a background job
        val collectedValues = mutableListOf<FloatArray>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            fakeSensor.sensorFlow.collect {
                collectedValues.add(it)
            }
        }

        // 3. Simulate sensor data
        fakeSensor.emitData(expectedValues)

        // 4. Assert that the flow emitted the data
        assertArrayEquals(expectedValues, collectedValues.first(), 0.0f)

        // Cleanup
        job.cancel()
    }
}