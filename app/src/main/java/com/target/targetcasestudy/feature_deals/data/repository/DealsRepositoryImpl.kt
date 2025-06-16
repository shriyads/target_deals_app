package com.target.targetcasestudy.feature_deals.data.repository

import android.content.Context
import androidx.work.WorkManager
import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.core.utils.apiresult.ApiResultHandler
import com.target.targetcasestudy.feature_deals.data.local.dao.DealsDao
import com.target.targetcasestudy.feature_deals.data.mapper.DealsDtoToEntityMapper
import com.target.targetcasestudy.feature_deals.data.mapper.DealsEntityToDomainMapper
import com.target.targetcasestudy.feature_deals.data.remote.DealsAPI
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DealsRepositoryImpl @Inject constructor(
    private val apiService: DealsAPI,
    private val dealDao: DealsDao,
    private val apiResultHandler: ApiResultHandler,
    private val dealDtoToDealEntityMapper: DealsDtoToEntityMapper,
    private val dealEntityToDealMapper: DealsEntityToDomainMapper,
) : DealsRepository {


    companion object {
        const val OFFLINE_DATA_SYNC_WORK_NAME = "offline_data_sync_work"
    }

    override fun getDeals(): Flow<APIResult<List<Deals>>> = flow {
        emit(APIResult.Loading) // First, emit the loading state

        try {
            delay(1000L) // Simulate delay

            apiResultHandler.handleApiCall { apiService.getDealsList() }
                .collect { apiResult ->
                    when (apiResult) {
                        is APIResult.Loading -> { /* Handled by initial emit */
                        }

                        is APIResult.Success -> {
                            val dealEntities =
                                apiResult.data.deals?.map { dealDtoToDealEntityMapper.map(it) }
                            if (!dealEntities.isNullOrEmpty()) {
                                dealDao.insertAllDeals(dealEntities)
                                Timber.d("Inserted ${dealEntities.size} deals into DB.")

                            } else {
                                Timber.d("Empty or null dealEntities from API.")
                            }
                            // Do NOT emit APIResult.Success from here;  emit from DB.
                        }

                        is APIResult.Error -> {
                            Timber.e(
                                apiResult.exception,
                                "Error from API for getDeals: ${apiResult.apiMessage}"
                            )

                            emit(
                                APIResult.Error(
                                    apiResult.exception,
                                    apiResult.apiErrorCode,
                                    apiResult.apiMessage
                                )
                            )
                        }
                    }
                }
            // Always emit local DB as source of truth after attempting API call
            dealDao.getAllDeals()
                .map { entities ->
                    val domainDeals = entities.map { dealEntityToDealMapper.map(it) }
                    APIResult.Success(domainDeals) // Emit Success with data from DB
                }
                .catch { e ->
                    Timber.e(e, "Error reading DB for getDeals")
                    emit(
                        APIResult.Error(
                            IOException("Failed to read from DB"),
                            apiMessage = "Local DB error"
                        )
                    )
                }
                .collect { result ->
                    emit(result) // This will be APIResult.Success or APIResult.Error from DB
                }
        } catch (e: Exception) {
            Timber.e(e, "Unexpected error in getDeals repository flow")
            emit(APIResult.Error(e, apiMessage = "Unexpected error occurred while fetching deals"))
        }
    }


    override fun searchDeals(query: String): Flow<List<Deals>> {
        return dealDao.searchDeals(query)
            .map { it.map { entity -> dealEntityToDealMapper.map(entity) } }
    }

    override fun getDealDetails(dealId: String): Flow<APIResult<Deals>> = flow {
        emit(APIResult.Loading) // Start by emitting a loading state
        try {
            delay(1000L) // Simulate network/processing delay for deal details

            apiResultHandler.handleApiCall { apiService.getDeal(dealId) }
                .collect { apiResult ->
                    when (apiResult) {
                        is APIResult.Loading -> { /* Handled by initial emit */
                        }

                        is APIResult.Success -> {
                            val dealDto = apiResult.data
                            val dealEntity = dealDtoToDealEntityMapper.map(dealDto)
                            dealDao.insertDeal(dealEntity)
                            Timber.d("Fetched and inserted deal $dealId from API into DB.")

                        }

                        is APIResult.Error -> {
                            Timber.e(
                                apiResult.exception,
                                "Error fetching deal $dealId from API: ${apiResult.apiMessage}"
                            )
                            emit(
                                APIResult.Error(
                                    apiResult.exception,
                                    apiResult.apiErrorCode,
                                    apiResult.apiMessage
                                )
                            )
                        }
                    }
                }

            dealDao.getDealById(dealId)
                .map { entity ->
                    if (entity != null) {
                        APIResult.Success(dealEntityToDealMapper.map(entity))
                    } else {
                        APIResult.Error(
                            NoSuchElementException("Deal with ID $dealId not found."),
                            apiMessage = "Deal not found"
                        )
                    }
                }
                .catch { e ->
                    Timber.e(e, "Error reading deal $dealId from local DB")
                    emit(
                        APIResult.Error(
                            IOException("Failed to read deal from local DB."),
                            apiMessage = "Local DB error"
                        )
                    )
                }
                .collect { result ->
                    emit(result)
                }
        } catch (e: Exception) {
            Timber.e(e, "Unexpected error in getDealDetails for deal ID: $dealId")
            emit(
                APIResult.Error(
                    e,
                    apiMessage = "Unexpected error occurred while fetching deal details."
                )
            )
        }
    }
}
