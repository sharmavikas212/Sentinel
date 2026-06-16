package com.vikas.sentinel.data.room

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SensorSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val sensorDao: SensorDao,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // 1. Fetch unsynced data from Room
            // (Note: You'll need to add an 'isSynced' flag to your Room entities)
            val unsyncedReadings = sensorDao.getUnsyncedLightReadings()
            Log.d("SensorSyncWorker", "Worker triggered! Found ${unsyncedReadings.size} readings")

            if (unsyncedReadings.isEmpty()) return Result.success()

            // 2. Map to DTO for cloud upload
//            val payload = SensorSyncPayloadDto(
//                deviceId = "unique_device_id", // Should be fetched from a Secure storage
//                readings = unsyncedReadings.map { it.toDto() }
//            )

            // 3. TODO: Perform actual network upload
            // val response = apiService.uploadReadings(payload)

            // 4. On success, mark as synced in local DB
            sensorDao.markAsSynced(unsyncedReadings.map { it.id })
            Log.d("SensorSyncWorker", "Sync successful for ${unsyncedReadings.size} items")
            
            return Result.success()
        } catch (e: Exception) {
            // Retry if it's a transient network error
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}