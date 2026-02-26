package com.vikas.sentinel.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vikas.sentinel.ui.theme.SentinelTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SentinelTheme {
                val homeViewModel = viewModel<HomeViewModel>()


                // Collect the StateFlows.
                // When app goes to background, this stops collecting.
                // This causes the ViewModel's WhileSubscribed to timeout.
                // Which causes the MeasurableSensor's awaitClose to fire.
                // Which calls sensorManager.unregisterListener().

                val light by homeViewModel.lightValue.collectAsStateWithLifecycle()
                val proximity by homeViewModel.proximityValue.collectAsStateWithLifecycle()
                val accelerometer by homeViewModel.accelerometerValue.collectAsStateWithLifecycle()
                val gyroscope by homeViewModel.gyroscopeValue.collectAsStateWithLifecycle()
                val pressure by homeViewModel.pressureValue.collectAsStateWithLifecycle()
                val magnetometer by homeViewModel.magnetometerValue.collectAsStateWithLifecycle()
                val temp by homeViewModel.ambientTemperatureValue.collectAsStateWithLifecycle()
                val humidity by homeViewModel.humidityValue.collectAsStateWithLifecycle()


                Home(
                    lightReading = light,
                    proximityReading = proximity,
                    accelerometerReading = accelerometer,
                    gyroscopeReading = gyroscope,
                    pressureReading = pressure,
                    magnetometerReading = magnetometer,
                    ambientTemperatureReading = temp,
                    humidityReading = humidity
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun Home(
    modifier: Modifier = Modifier,
    lightReading: Float = 0f,
    proximityReading: Float = 0f,
    accelerometerReading: FloatArray = FloatArray(3),
    gyroscopeReading: FloatArray = FloatArray(3),
    pressureReading: Float = 0f,
    magnetometerReading: FloatArray = FloatArray(3),
    ambientTemperatureReading: Float = 0f,
    humidityReading: Float = 0f
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text("Sensor Name", modifier = Modifier.weight(1f))
            Text("Sensor Value", modifier = Modifier.weight(1f))
        }
        HorizontalDivider()
        SensorReading("Light Sensor", lightReading)
        SensorReading("Proximity Sensor", proximityReading)
        SensorReading("Accelerometer Sensor", accelerometerReading)
        SensorReading("Gyroscope Sensor", gyroscopeReading)
        SensorReading("Pressure Sensor", pressureReading)
        SensorReading("Magnetometer Sensor", magnetometerReading)
        SensorReading("Ambient Temperature Sensor", ambientTemperatureReading)
        SensorReading("Humidity Sensor", humidityReading)
    }
}

@Composable
fun SensorReading(name: String, reading: Float) {
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = name, modifier = Modifier.weight(1f))
        Text(text = "%.2f".format(reading), modifier = Modifier.weight(1f))
    }
}

@Composable
fun SensorReading(name: String, reading: FloatArray) {
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = name, modifier = Modifier.weight(1f))
        Text(text = reading.joinToString(",") { "%.2f".format(it) }, modifier = Modifier.weight(1f), maxLines = 1)
    }
}