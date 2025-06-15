package com.target.targetcasestudy.feature_deals.dealslistofflineworker

import androidx.work.*
import javax.inject.Inject

class DealsSyncManager @Inject constructor(
    private val workManager: WorkManager
) {
    companion object {
        private const val DEALS_LIST_SYNC_WORK_NAME = "deals_list_sync"
    }

    fun scheduleDealsListSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when internet is available
            .build()

        val request = OneTimeWorkRequestBuilder<DealsListOfflineOfflineSyncWorker>()
            .setConstraints(constraints)
            .addTag(DEALS_LIST_SYNC_WORK_NAME)
            .build()

        workManager.enqueueUniqueWork(
            DEALS_LIST_SYNC_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
