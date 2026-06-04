package com.vikas.sentinel.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vikas.sentinel.domain.model.SensorUnit

@Entity
data class Light(
    val value: Float,
    val unit: SensorUnit,
    val isSynced: Boolean,
    @PrimaryKey val timestamp: Long = System.currentTimeMillis()
)
