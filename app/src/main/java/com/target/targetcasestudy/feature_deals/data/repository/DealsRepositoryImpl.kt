package com.target.targetcasestudy.feature_deals.data.repository

import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.core.utils.apiresult.ApiResultHandler
import com.target.targetcasestudy.feature_deals.data.local.dao.DealsDao
import com.target.targetcasestudy.feature_deals.data.mapper.DealsDtoToEntityMapper
import com.target.targetcasestudy.feature_deals.data.mapper.DealsEntityToDomainMapper
import com.target.targetcasestudy.feature_deals.data.remote.DealsAPI
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    override fun getDeals(): Flow<APIResult<List<Deals>>> = flow {
        emit(APIResult.Loading)

        val apiResult = apiResultHandler.handleApiCall { apiService.getDealsList() }
            .first { it !is APIResult.Loading }

        if (apiResult is APIResult.Success) {
            val entities = apiResult.data.deals
                ?.map { dealDtoToDealEntityMapper.map(it) }

            if (!entities.isNullOrEmpty()) {
                dealDao.deleteAllDeals()
                dealDao.refreshDeals(entities)
                Timber.d("Inserted ${entities.size} deals into DB.")
            } else {
                Timber.d("Empty or null deals received from API.")
            }
        } else if (apiResult is APIResult.Error) {
            Timber.e(apiResult.exception, "API error while fetching deals: ${apiResult.apiMessage}")
        }

        emitAll(
            dealDao.getAllDeals()
                .map { entities ->
                    if (entities.isNotEmpty()) {
                        APIResult.Success(entities.map(dealEntityToDealMapper::map))
                    } else if (apiResult is APIResult.Error) {
                        APIResult.Error(
                            apiResult.exception,
                            apiResult.apiErrorCode,
                            apiResult.apiMessage
                        )
                    } else {
                        APIResult.Success(emptyList())
                    }
                }
                .catch { e ->
                    Timber.e(e, "Error reading deals from DB.")
                    emit(
                        APIResult.Error(
                            IOException("Failed to read deals from DB."),
                            apiMessage = "Local DB error"
                        )
                    )
                }
        )
    }.flowOn(Dispatchers.IO)

    override fun searchDeals(query: String): Flow<List<Deals>> {
        return dealDao.searchDeals(query)
            .map { it.map(dealEntityToDealMapper::map) }
            .flowOn(Dispatchers.IO)

    }

    override fun getDealDetails(dealId: String): Flow<APIResult<Deals>> = flow {
        emit(APIResult.Loading)

        val apiResult = apiResultHandler.handleApiCall { apiService.getDeal(dealId) }
            .first { it !is APIResult.Loading }

        if (apiResult is APIResult.Success) {
            val dealEntity = dealDtoToDealEntityMapper.map(apiResult.data)
            dealDao.insertDeal(dealEntity)
            Timber.d("Fetched and inserted deal $dealId from API.")
        } else if (apiResult is APIResult.Error) {
            Timber.e(
                apiResult.exception,
                "API error while fetching deal $dealId: ${apiResult.apiMessage}"
            )
        }

        emitAll(
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
                    Timber.e(e, "DB error fetching deal $dealId.")
                    emit(
                        APIResult.Error(
                            IOException("Failed to read deal from DB."),
                            apiMessage = "Local DB error"
                        )
                    )
                }
        )
    }.flowOn(Dispatchers.IO)
}

