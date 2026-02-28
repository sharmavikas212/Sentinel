package com.vikas.sentinel.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.himanshoe.charty.color.ChartyColor
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.LineChartConfig
import com.himanshoe.charty.line.data.LineData
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
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
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
        Spacer(modifier = Modifier.height(16.dp))
        Compass(magnetometerReading)
        LightSensorReadingChart(lightReading)
        AccelerometerMagnitudeChart(accelerometerReading)
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
fun Compass(magnetometerReading: FloatArray) {
    // Calculate azimuth (angle relative to magnetic north)
    // Using atan2 on X and Y components of the magnetometer
    val azimuth = Math.toDegrees(
        kotlin.math.atan2(magnetometerReading[0].toDouble(), magnetometerReading[1].toDouble())
    ).toFloat()

    val direction = when (((azimuth + 360) % 360)) {
        in 22.5..67.5 -> "NW"
        in 67.5..112.5 -> "W"
        in 112.5..157.5 -> "SW"
        in 157.5..202.5 -> "S"
        in 202.5..247.5 -> "SE"
        in 247.5..292.5 -> "E"
        in 292.5..337.5 -> "NE"
        else -> "N"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("1. Using Magnetometer", fontWeight = FontWeight.Bold)
        Text(
            text = "${((azimuth + 360) % 360).toInt()}° $direction",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Simple visual representation of a needle
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("N", modifier = Modifier.align(Alignment.TopCenter), color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("S", modifier = Modifier.align(Alignment.BottomCenter), fontWeight = FontWeight.Bold)
            Text("E", modifier = Modifier.align(Alignment.CenterEnd), fontWeight = FontWeight.Bold)
            Text("W", modifier = Modifier.align(Alignment.CenterStart), fontWeight = FontWeight.Bold)
            // Only the needle (arrow box) rotates
            Box(
                modifier = Modifier
                    .size(2.dp, 60.dp)
                    .rotate(-azimuth)
                    .background(Color.Black)
            ) {
                // Arrow head
                Box(modifier = Modifier.size(6.dp).align(Alignment.TopCenter).rotate(45f).background(Color.Red))
                Box(modifier = Modifier.size(6.dp).align(Alignment.TopCenter).rotate(135f).background(Color.Red))
            }
        }
    }
}

@Composable
fun AccelerometerMagnitudeChart(reading: FloatArray) {
    Text("2. Using Accelerometer", fontWeight = FontWeight.Bold)

    val chartData = remember { mutableStateListOf<LineData>() }

    LaunchedEffect(reading) {
        // Calculate magnitude (vector length) of the acceleration
        val magnitude = kotlin.math.sqrt(
            reading[0] * reading[0] + reading[1] * reading[1] + reading[2] * reading[2]
        )
        chartData.add(LineData("", magnitude))
        if (chartData.size > 100) {
            chartData.removeAt(0)
        }
    }

    val currentData = chartData.toList()

    if (chartData.isNotEmpty()) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 16.dp),
            data = { currentData },
            color = ChartyColor.Solid(Color.Red),
            lineConfig = LineChartConfig(lineWidth = 10f, showPoints = false, smoothCurve = true)
        )
    }
}
@Composable
fun SensorReading(name: String, reading: FloatArray) {
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = name, modifier = Modifier.weight(1f))
        Text(text = reading.joinToString(",") { "%.2f".format(it) }, modifier = Modifier.weight(1f), maxLines = 1)
    }
}

@Composable
fun LightSensorReadingChart(reading: Float) {
    Text("3. Using Light Sensor", fontWeight = FontWeight.Bold)
    val chartData = remember { mutableStateListOf<LineData>() }

    LaunchedEffect(reading) {
        chartData.add(LineData("", reading))
        if (chartData.size > 100) {
            chartData.removeAt(0)
        }
    }
    
    if (chartData.isNotEmpty()) {
        // Create a copy of the list to force recomposition if needed,
        // although mutableStateListOf should handle it.
        // The issue might be that LineChart expects a new list instance or specific data structure.
        // Let's try passing a new list instance.
        val currentData = chartData.toList()
        
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            data = { currentData },
            color = ChartyColor.Solid(Color(Color.Blue.value)),
            lineConfig = LineChartConfig(
                lineWidth = 10f,
                showPoints = true,
                smoothCurve = true,
            ),
        )
    }
}
