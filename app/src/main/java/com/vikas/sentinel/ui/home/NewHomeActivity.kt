package com.vikas.sentinel.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val CardBgd = Color(0xFFAEB5C9)

class NewHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @Preview(showBackground = true)
    @Composable
    fun Dashboard(){
        WaveCard(Modifier,
            Icons.Default.DeviceThermostat
        )
    }

    @Composable
    fun WaveCard(modifier: Modifier,
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
                Row() {
                    Text("Temperature")
                    Icon(icon,
                        null,
                        tint = Color(0xFFE5175C),

                    )
                }
            }
        }
    }
}