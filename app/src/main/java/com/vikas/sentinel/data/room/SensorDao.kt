package com.vikas.sentinel.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SensorDao {
    @Insert
    suspend fun insertLight(vararg light: Light)

    @Query("SELECT * FROM Light WHERE isSynced = 0 LIMIT 100")
    suspend fun getUnsyncedLightReadings(): List<Light>

    @Query("UPDATE Light SET isSynced = 1 WHERE timestamp IN (:timestamp)")
    suspend fun markAsSynced(timestamp: List<Long>)

    @Query("DELETE FROM Light WHERE isSynced = 1")
    suspend fun deleteSyncedReadings()
}