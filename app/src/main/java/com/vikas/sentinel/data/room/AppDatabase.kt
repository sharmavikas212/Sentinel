package com.vikas.sentinel.data.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Light::class, Humidity::class, Pressure::class, Proximity::class, Temperature::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sensorDao() : SensorDao
}