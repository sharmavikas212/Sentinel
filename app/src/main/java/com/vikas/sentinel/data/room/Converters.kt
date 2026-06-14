package com.vikas.sentinel.data.room

import androidx.room.TypeConverter
import com.vikas.sentinel.domain.model.SensorUnit

class Converters {

    // Converter for List<Float>
    @TypeConverter
    fun fromFloatList(value: List<Float>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toFloatList(value: String): List<Float> {
        return if (value.isEmpty()) emptyList() else value.split(",").map { it.toFloat() }
    }

    // Converter for FloatArray
    @TypeConverter
    fun fromFloatArray(value: FloatArray): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toFloatArray(value: String): FloatArray {
        return if (value.isEmpty()) floatArrayOf() else value.split(",").map { it.toFloat() }.toFloatArray()
    }


    // Converter for SensorUnit Enum
    @TypeConverter
    fun fromSensorUnit(unit: SensorUnit): String {
        return unit.name
    }

    @TypeConverter
    fun toSensorUnit(value: String): SensorUnit {
        return try {
            SensorUnit.valueOf(value)
        } catch (e: IllegalArgumentException) {
            SensorUnit.UNKNOWN
        }
    }
}