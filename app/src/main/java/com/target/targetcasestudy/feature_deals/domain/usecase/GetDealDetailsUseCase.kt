package com.target.targetcasestudy.feature_deals.domain.usecase

import com.target.targetcasestudy.core.domain.BaseUseCase
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDealDetailsUseCase @Inject constructor(
    private val dealRepository: DealsRepository
) : BaseUseCase<String, APIResult<Deals?>> {

    override fun invoke(dealId: String): Flow<APIResult<Deals?>> {
        return dealRepository.getDealDetails(dealId)
    }
}
