package com.target.targetcasestudy.core.offlinework

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import timber.log.Timber

abstract class BaseOfflineSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Timber.d("${this::class.java.simpleName}: doWork() started.")
        return try {
            when (syncFeature()) {
                true -> {
                    Timber.d("${this::class.java.simpleName}: Sync successful.")
                    Result.success()
                }

                false -> {
                    Timber.w("${this::class.java.simpleName}: Sync failed. Retrying...")
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "${this::class.java.simpleName}: Exception during sync.")
            Result.retry()
        }
    }

    // Force subclasses to implement their sync logic
    abstract suspend fun syncFeature(): Boolean
}

