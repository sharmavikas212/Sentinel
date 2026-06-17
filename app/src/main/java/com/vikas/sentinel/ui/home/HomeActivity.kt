package com.vikas.sentinel.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WbIridescent
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vikas.sentinel.domain.model.EnvironmentalReading
import com.vikas.sentinel.domain.model.SensorUnit
import com.vikas.sentinel.domain.model.VectorReading
import com.vikas.sentinel.ui.theme.SentinelTheme
import dagger.hilt.android.AndroidEntryPoint

// Dashboard Theme Colors - Matching Dark UI Image
val DashboardBg = Color(0xFF0F111A)
val CardBg = Color(0xFF1B1F2B)
val CyanAccent = Color(0xFF00E5FF)
val OrangeAccent = Color(0xFFFB8C00)
val GreenAccent = Color(0xFF00E676)
val BlueAccent = Color(0xFF2196F3)
val PurpleAccent = Color(0xFFBB86FC)
val TextGray = Color(0xFF8E94A3)
val BorderColor = Color.White.copy(alpha = 0.05f)

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
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

                DashboardScreen(
                    light = light,
                    battery = battery,
                    temp = temp,
                    humidity = humidity,
                    proximity = proximity,
                    pressure = pressure,
                    accel = accel,
                    gyro = gyro,
                    magneto = magneto
                )
            }
        }
    }
}

@Composable
fun DashboardScreen(
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
    Scaffold(containerColor = DashboardBg) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            HeaderSection()
            Spacer(modifier = Modifier.height(32.dp))

            // Main Stat Cards Grid (2x2)
            Row(modifier = Modifier.fillMaxWidth()) {
                SensorCard(
                    title = "Ambient Temp",
                    value = "%.1f".format(temp.value),
                    unit = "°C",
                    icon = Icons.Default.DeviceThermostat,
                    accent = OrangeAccent,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                SensorCard(
                    title = "Battery Level",
                    value = "${battery.value.toInt()}",
                    unit = "%",
                    icon = Icons.Default.BatteryFull,
                    accent = CyanAccent,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                SensorCard(
                    title = "Light Level",
                    value = "%.0f".format(light.value),
                    unit = "lx",
                    icon = Icons.Default.WbIridescent,
                    accent = BlueAccent,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                SensorCard(
                    title = "Humidity",
                    value = "%.1f".format(humidity.value),
                    unit = "%",
                    icon = Icons.Default.Opacity,
                    accent = GreenAccent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // Motion and Magnetic Sensors Section
            MotionSensorsSection(accel, gyro, magneto)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Environmental Extras
            Row(modifier = Modifier.fillMaxWidth()) {
                MiniSensorCard("Proximity", "%.1f".format(proximity.value), "cm", Icons.Default.Radar, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                MiniSensorCard("Pressure", "%.0f".format(pressure.value), "hPa", Icons.Default.Speed, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))
            SystemLoadHistory()
            Spacer(modifier = Modifier.height(32.dp))
            RealTimeEventLog()
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Sentinel Platform",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            HeaderStatusChip("SENSORS ACTIVE", GreenAccent)
            Spacer(modifier = Modifier.width(12.dp))
            HeaderStatusChip("SECURE", CyanAccent)
        }
    }
}

@Composable
fun HeaderStatusChip(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = TextGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SensorCard(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    accent: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier.height(180.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.2f))
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Icon(icon, null, tint = accent.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
            }
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = value,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                    if (unit.isNotEmpty()) {
                        Text(
                            text = " $unit",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Sparkline(accent)
            }
        }
    }
}

@Composable
fun MiniSensorCard(title: String, value: String, unit: String, icon: ImageVector, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = PurpleAccent, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
            Column {
                Text(title, color = TextGray, fontSize = 10.sp)
                Text("$value $unit", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MotionSensorsSection(accel: VectorReading, gyro: VectorReading, magneto: VectorReading) {
    Column {
        Text(
            text = "Motion & Magnetic Field",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(16.dp))
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderColor)
        ) {
            Column(Modifier.padding(20.dp)) {
                VectorDataRow("Accelerometer", accel, CyanAccent)
                Spacer(Modifier.height(12.dp))
                VectorDataRow("Gyroscope", gyro, OrangeAccent)
                Spacer(Modifier.height(12.dp))
                VectorDataRow("Magnetometer", magneto, GreenAccent)
            }
        }
    }
}

@Composable
fun VectorDataRow(label: String, vector: VectorReading, color: Color) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(4.dp).clip(CircleShape).background(color))
            Spacer(Modifier.width(8.dp))
            Text(label, color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Text(
            "X:%.1f Y:%.1f Z:%.1f".format(vector.x, vector.y, vector.z),
            color = Color.White,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun Sparkline(color: Color) {
    Canvas(Modifier
        .fillMaxWidth()
        .height(40.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.7f)
            lineTo(size.width * 0.15f, size.height * 0.7f)
            lineTo(size.width * 0.25f, size.height * 0.2f)
            lineTo(size.width * 0.35f, size.height * 0.9f)
            lineTo(size.width * 0.45f, size.height * 0.7f)
            lineTo(size.width * 0.7f, size.height * 0.7f)
            lineTo(size.width * 0.8f, size.height * 0.4f)
            lineTo(size.width, size.height * 0.7f)
        }
        drawPath(path, color, style = Stroke(width = 2.dp.toPx()))
    }
}

@Composable
fun SystemLoadHistory() {
    Column {
        Text(
            text = "System Load History",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(16.dp))
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderColor)
        ) {
            Column(Modifier.padding(20.dp)) {
                Box(Modifier
                    .fillMaxWidth()
                    .height(140.dp)) {
                    HistoryChart()
                }
            }
        }
    }
}

@Composable
fun HistoryChart() {
    Canvas(Modifier.fillMaxSize()) {
        // Grid Lines
        val gridLines = 5
        for (i in 0 until gridLines) {
            val y = size.height * (i.toFloat() / (gridLines - 1))
            drawLine(
                color = Color.White.copy(alpha = 0.1f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }

        // Data Path
        val path = Path().apply {
            moveTo(0f, size.height * 0.7f)
            quadraticTo(size.width * 0.1f, size.height * 0.6f, size.width * 0.2f, size.height * 0.8f)
            lineTo(size.width * 0.3f, size.height * 0.4f)
            lineTo(size.width * 0.4f, size.height * 0.5f)
            lineTo(size.width * 0.5f, size.height * 0.3f)
            lineTo(size.width * 0.6f, size.height * 0.6f)
            lineTo(size.width * 0.7f, size.height * 0.2f)
            lineTo(size.width * 0.8f, size.height * 0.3f)
            lineTo(size.width * 0.9f, size.height * 0.1f)
            lineTo(size.width, size.height * 0.4f)
        }
        drawPath(path, CyanAccent, style = Stroke(width = 2.dp.toPx()))
        
        // Draw points
        drawCircle(CyanAccent, 4.dp.toPx(), Offset(size.width * 0.3f, size.height * 0.4f))
        drawCircle(CyanAccent, 4.dp.toPx(), Offset(size.width * 0.7f, size.height * 0.2f))
        drawCircle(CyanAccent, 4.dp.toPx(), Offset(size.width * 0.9f, size.height * 0.1f))
    }
}

@Composable
fun RealTimeEventLog() {
    Column {
        Text(
            text = "Real-time Event Log",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(16.dp))
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderColor)
        ) {
            Column(Modifier.padding(16.dp)) {
                LogItem("2026 01 30 14:35:12 - Telemetry sync to AWS initialized", GreenAccent)
                LogItem("2026 01 30 14:34:58 - Thermal threshold exceeded 40°C", OrangeAccent)
                LogItem("Network reconnected.", GreenAccent)
            }
        }
    }
}

@Composable
fun LogItem(text: String, color: Color) {
    Row(
        Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            color = if (color == OrangeAccent) color else TextGray,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 18.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F111A)
@Composable
fun DashboardPreview() {
    SentinelTheme {
        DashboardScreen()
    }
}
