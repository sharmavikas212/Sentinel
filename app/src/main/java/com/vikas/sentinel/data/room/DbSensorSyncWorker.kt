package com.vikas.sentinel.data.room

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

//to be implemented later when even the data stored in the db will be batched and processed to store
@HiltWorker
class DbSensorSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val sensorDao: SensorDao
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return TODO(
            "batch and store either " +
                    "1. by converting to a json and passing it to worker form repository implementation " +
                    "2. or by storing the data to file form repository implementation and then fetch that file in worker to store in db"
        )
    }
}