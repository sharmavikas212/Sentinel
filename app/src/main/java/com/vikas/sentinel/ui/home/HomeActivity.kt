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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
                val batteryPercentage by homeViewModel.batteryPercentage.collectAsStateWithLifecycle()


                Home(
                    lightReading = light,
                    proximityReading = proximity,
                    accelerometerReading = accelerometer,
                    gyroscopeReading = gyroscope,
                    pressureReading = pressure,
                    magnetometerReading = magnetometer,
                    ambientTemperatureReading = temp,
                    humidityReading = humidity,
                    batteryPercentage = batteryPercentage
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
    humidityReading: Float = 0f,
    batteryPercentage: Float = 0f
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            "Sentinel Dashboard",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1C1E),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Compass(magnetometerReading)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LightSensorReadingChart(lightReading, modifier = Modifier.weight(1f))
            AccelerometerMagnitudeChart(accelerometerReading, modifier = Modifier.weight(1f))
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SensorReading("Light", lightReading, "lx")
                SensorReading("Proximity", proximityReading, "cm")
                SensorReading("Pressure", pressureReading, "hPa")
                SensorReading("Temperature", ambientTemperatureReading, "°C")
                SensorReading("Humidity", humidityReading, "%")
                SensorReading("Battery", batteryPercentage, "%")
                SensorReading("Accelerometer", accelerometerReading)
                SensorReading("Gyroscope", gyroscopeReading)
                SensorReading("Magnetometer", magnetometerReading)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SensorReading(name: String, reading: Float, unit: String = "") {
    Row(
        modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name, fontWeight = FontWeight.Medium, color = Color.Gray)
        Text(
            text = "%.2f %s".format(reading, unit),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3)
        )
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp).fillMaxWidth()
        ) {
            Text("Compass", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = "${((azimuth + 360) % 360).toInt()}° $direction",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE91E63)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("N", modifier = Modifier.align(Alignment.TopCenter), color = Color.Red, fontWeight = FontWeight.Bold)
                Text("S", modifier = Modifier.align(Alignment.BottomCenter), fontWeight = FontWeight.Bold)
                Text("E", modifier = Modifier.align(Alignment.CenterEnd), fontWeight = FontWeight.Bold)
                Text("W", modifier = Modifier.align(Alignment.CenterStart), fontWeight = FontWeight.Bold)

                Box(
                    modifier = Modifier
                        .size(4.dp, 80.dp)
                        .rotate(-azimuth)
                        .background(Color.DarkGray, RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}

@Composable
fun AccelerometerMagnitudeChart(reading: FloatArray, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Motion Intensity", fontWeight = FontWeight.Bold, fontSize = 16.sp)

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
                .height(150.dp)
                .padding(top = 16.dp),
            data = { currentData },
            color = ChartyColor.Solid(Color.Red),
            lineConfig = LineChartConfig(lineWidth = 10f, showPoints = false, smoothCurve = true)
        )
    }
        }
    }
}
@Composable
fun SensorReading(name: String, reading: FloatArray) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name, fontWeight = FontWeight.Medium, color = Color.Gray)
        Text(
            text = reading.joinToString(", ") { "%.1f".format(it) },
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50),
            maxLines = 1
        )
    }
}

@Composable
fun LightSensorReadingChart(reading: Float, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Light Exposure", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    val chartData = remember { mutableStateListOf<LineData>() }

    LaunchedEffect(reading) {
        chartData.add(LineData("", reading))
        if (chartData.size > 100) {
            chartData.removeAt(0)
        }
    }
    
    if (chartData.isNotEmpty()) {
        val currentData = chartData.toList()
        
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 16.dp),
            data = { currentData },
            color = ChartyColor.Solid(Color(0xFFFFC107)),
            lineConfig = LineChartConfig(
                lineWidth = 10f,
                showPoints = false,
                smoothCurve = true,
            ),
        )
    }
        }
    }
}
