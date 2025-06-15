package com.target.targetcasestudy.feature_deals.domain.repository

import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import kotlinx.coroutines.flow.Flow

interface DealsRepository {
    fun getDeals(): Flow<APIResult<List<Deals>>>
    fun searchDeals(query: String): Flow<List<Deals>>
    fun getDealDetails(dealId: String): Flow<APIResult<Deals>>
}