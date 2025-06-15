package com.target.targetcasestudy.feature_deals.dealslistofflineworker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import com.target.targetcasestudy.core.offlinework.BaseOfflineSyncWorker
import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.feature_deals.domain.usecase.GetDealsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber

@HiltWorker
class DealsListOfflineOfflineSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getDealsUseCase: GetDealsUseCase
) : BaseOfflineSyncWorker(context, params) {

    override suspend fun syncFeature(): Boolean {
        Timber.d("DealsListOfflineSyncWorker: Sync started.")

        val result = getDealsUseCase(Unit).first { it !is APIResult.Loading }

        return when (result) {
            is APIResult.Success -> {
                Timber.d("DealsListOfflineSyncWorker: Sync success with ${result.data.size} deals.")
                true
            }
            is APIResult.Error -> {
                Timber.w("DealsListOfflineSyncWorker: Sync failed: ${result.apiMessage}")
                false
            }
            else -> {
                Timber.e("DealsListOfflineSyncWorker: Unexpected APIResult state.")
                false
            }
        }
    }
}
