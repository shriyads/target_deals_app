package com.target.targetcasestudy.feature_deals.domain.usecase

import com.target.targetcasestudy.core.domain.BaseUseCase
import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDealsUseCase @Inject constructor(
    private val dealRepository: DealsRepository
) : BaseUseCase<Unit, APIResult<List<Deals>>> {

    override fun invoke(params: Unit): Flow<APIResult<List<Deals>>> {
        return dealRepository.getDeals()
    }
}
