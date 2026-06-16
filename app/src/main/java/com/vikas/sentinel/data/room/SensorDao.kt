package com.vikas.sentinel.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SensorDao {
    @Insert
    suspend fun insertSensorReading(vararg sensorRecord: SensorRecord)

    @Query("SELECT * FROM sensor_records WHERE isSynced = 0 LIMIT 500")
    suspend fun getUnsyncedLightReadings(): List<SensorRecord>

    @Query("UPDATE sensor_records SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("DELETE FROM sensor_records WHERE isSynced = 1")
    suspend fun deleteSyncedReadings()
}