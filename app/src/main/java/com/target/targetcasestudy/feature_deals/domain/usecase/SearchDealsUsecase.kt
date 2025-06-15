package com.target.targetcasestudy.feature_deals.domain.usecase

import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.core.domain.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchDealsUseCase @Inject constructor(
    private val repository: DealsRepository
) : BaseUseCase<String, List<Deals>> {

    override fun invoke(params: String): Flow<List<Deals>> {
        return repository.searchDeals(params)
    }
}

