package com.target.targetcasestudy.core.di

import android.content.Context

import com.target.targetcasestudy.feature_deals.data.local.dao.DealsDao
import com.target.targetcasestudy.feature_deals.data.mapper.DealsDtoToEntityMapper
import com.target.targetcasestudy.feature_deals.data.mapper.DealsEntityToDomainMapper
import com.target.targetcasestudy.feature_deals.data.remote.DealsAPI
import com.target.targetcasestudy.core.utils.apiresult.ApiResultHandler
import com.target.targetcasestudy.core.utils.apiresult.ApiResultHandlerImpl
import com.target.targetcasestudy.feature_deals.data.repository.DealsRepositoryImpl
import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideApiResultHandler(): ApiResultHandler {
        return ApiResultHandlerImpl()
    }
    @Provides
    @Singleton
    fun provideDealRepository(
        apiService: DealsAPI,
        dealDao: DealsDao,
        apiResultHandler: ApiResultHandler,
        dealDtoToDealEntityMapper: DealsDtoToEntityMapper,
        dealEntityToDealMapper: DealsEntityToDomainMapper,
    ): DealsRepository {
        return DealsRepositoryImpl(
            apiService,
            dealDao,
            apiResultHandler,
            dealDtoToDealEntityMapper,
            dealEntityToDealMapper,

        )
    }
}