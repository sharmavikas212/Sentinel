package com.vikas.sentinel.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vikas.sentinel.domain.model.EnvironmentalReading
import com.vikas.sentinel.domain.model.SensorUnit
import com.vikas.sentinel.domain.model.VectorReading
import com.vikas.sentinel.ui.theme.SentinelTheme
import dagger.hilt.android.AndroidEntryPoint

val CardBgd = Color(0xFF1B1F2B)

@AndroidEntryPoint
class NewHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SentinelTheme {
                val viewModel = viewModel<HomeViewModel>()

                val light by viewModel.lightValue.collectAsStateWithLifecycle()
                val battery by viewModel.batteryPercentage.collectAsStateWithLifecycle()
                val temp by viewModel.ambientTemperatureValue.collectAsStateWithLifecycle()
                val humidity by viewModel.humidityValue.collectAsStateWithLifecycle()
                val proximity by viewModel.proximityValue.collectAsStateWithLifecycle()
                val pressure by viewModel.pressureValue.collectAsStateWithLifecycle()
                val accel by viewModel.accelerometerValue.collectAsStateWithLifecycle()
                val gyro by viewModel.gyroscopeValue.collectAsStateWithLifecycle()
                val magneto by viewModel.magnetometerValue.collectAsStateWithLifecycle()
                Dashboard(light,battery,temp,humidity,proximity,pressure,accel,gyro,magneto)
            }
        }

    }


    @Preview(showBackground = true)
    @Composable
    fun Dashboard(
        light: EnvironmentalReading = EnvironmentalReading(1.810f, SensorUnit.LUX),
        battery: EnvironmentalReading = EnvironmentalReading(50f, SensorUnit.PERCENT),
        temp: EnvironmentalReading = EnvironmentalReading(65.1f, SensorUnit.CELSIUS),
        humidity: EnvironmentalReading = EnvironmentalReading(31.01f, SensorUnit.PERCENT),
        proximity: EnvironmentalReading = EnvironmentalReading(5.0f, SensorUnit.CM),
        pressure: EnvironmentalReading = EnvironmentalReading(1013.25f, SensorUnit.HPA),
        accel: VectorReading = VectorReading(0f, 0f, 0f),
        gyro: VectorReading = VectorReading(0f, 0f, 0f),
        magneto: VectorReading = VectorReading(0f, 0f, 0f)
    ) {

        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "Light",
                            "°C",
                            "%.1f".format(light.value),
                            Modifier.fillMaxWidth(),
                            Icons.Default.Brightness5
                        )
                    }
                    Spacer(Modifier.padding(5.dp))
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "battery",
                            "°C",
                            "${battery.value.toInt()}",
                            Modifier.fillMaxWidth(),
                            Icons.Default.DeviceThermostat
                        )
                    }
                }
                Row(modifier = Modifier.padding(16.dp)) {
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "temp",
                            "°C",
                            "%.1f".format(temp.value),
                            Modifier.fillMaxWidth(),
                            Icons.Default.DeviceThermostat
                        )
                    }
                    Spacer(Modifier.padding(5.dp))
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "humidity",
                            "°C",
                            "%.1f".format(humidity.value),
                            Modifier.fillMaxWidth(),
                            Icons.Default.DeviceThermostat
                        )
                    }
                }
                Row(modifier = Modifier.padding(16.dp)) {
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "proximity",
                            "°C",
                            "%.1f".format(proximity.value),
                            Modifier.fillMaxWidth(),
                            Icons.Default.DeviceThermostat
                        )
                    }
                    Spacer(Modifier.padding(5.dp))
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "pressure",
                            "°C",
                            "%.0f".format(pressure.value),
                            Modifier.fillMaxWidth(),
                            Icons.Default.DeviceThermostat
                        )
                    }
                }
                Row(modifier = Modifier.padding(16.dp)) {
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "accel",
                            "°C",
                            "65",
                            Modifier.fillMaxWidth(),
                            Icons.Default.DeviceThermostat
                        )
                    }
                    Spacer(Modifier.padding(5.dp))
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "gyro",
                            "°C",
                            "65",
                            Modifier.fillMaxWidth(),
                            Icons.Default.DeviceThermostat
                        )
                    }
                }
                Row(modifier = Modifier.padding(16.dp)) {
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "magneto",
                            "°C",
                            "65",
                            Modifier.fillMaxWidth(),
                            Icons.Default.DeviceThermostat
                        )
                    }
                    Spacer(Modifier.padding(5.dp))
                    Column(Modifier.weight(1f)) {
                        WaveCard(
                            "---------",
                            "°C",
                            "65",
                            Modifier.fillMaxWidth(),
                            Icons.Default.DeviceThermostat
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun WaveCard(
        sensorName: String,
        unit: String,
        reading: String,
        modifier: Modifier,
        icon: ImageVector,
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = CardBgd),
            shape = RoundedCornerShape(
                12.dp
            )
        ) {
            Column(
                Modifier
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(sensorName,
                        color = Color.White)
                    Spacer(Modifier.padding(10.dp))
                    Icon(
                        icon,
                        null,
                        tint = Color(0xFFE5175C),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Row(Modifier.padding(top = 8.dp)) {
                    Text("${reading}${unit}",
                        color = Color.White)
                }
            }
        }
    }
}