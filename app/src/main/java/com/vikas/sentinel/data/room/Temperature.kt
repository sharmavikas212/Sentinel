package com.vikas.sentinel.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vikas.sentinel.domain.model.SensorUnit

@Entity
data class Temperature(
    val value: Float,
    val unit: SensorUnit,
    @PrimaryKey val timestamp: Long = System.currentTimeMillis()
)
