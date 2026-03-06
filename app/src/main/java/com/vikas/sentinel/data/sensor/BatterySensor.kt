package com.vikas.sentinel.data.sensor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

abstract class BatterySensor(
    private val context: Context
) : MeasurableSensor(sensorType = 0) { // 0 is a placeholder as Battery has no Sensor.TYPE

    override val doesSensorExist: Boolean = true

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

                if (level != -1 && scale != -1) {
                    val percentage = (level * 100 / scale.toFloat())
                    // Emit the value to the flow
                    onSensorValueChanged?.invoke(floatArrayOf(percentage))
                }
            }
        }
    }

    override fun startListening() {
        // ACTION_BATTERY_CHANGED is a sticky broadcast, so registering it 
        // will immediately return the last known battery state.
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryReceiver, filter)
    }

    override fun stopListening() {
        try {
            context.unregisterReceiver(batteryReceiver)
        } catch (e: IllegalArgumentException) {
            // Receiver not registered or already unregistered
            e.printStackTrace()
        }
    }
}