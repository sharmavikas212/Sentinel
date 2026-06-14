package com.vikas.sentinel.data.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vikas.sentinel.domain.model.SensorUnit

/**
 * For keeping the insert time short we are
 */

@Entity(
    tableName = "sensor_records",
    indices = [Index(value = ["timestamp"])]
)
data class SensorRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val sensorType: String,
    val values: FloatArray,
    val unit: SensorUnit,
    val isSynced: Boolean,
)
