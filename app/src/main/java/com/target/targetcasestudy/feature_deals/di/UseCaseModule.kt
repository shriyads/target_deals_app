package com.target.targetcasestudy.feature_deals.di

import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import com.target.targetcasestudy.feature_deals.domain.usecase.GetDealDetailsUseCase
import com.target.targetcasestudy.feature_deals.domain.usecase.SearchDealsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {


    @Provides
    @ViewModelScoped
    fun provideSearchDealsUseCase(
        dealsRepository: DealsRepository
    ): SearchDealsUseCase {
        return SearchDealsUseCase(dealsRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDealDetailsUseCase(
        dealsRepository: DealsRepository
    ): GetDealDetailsUseCase {
        return GetDealDetailsUseCase(dealsRepository)
    }
}