package com.target.targetcasestudy.feature_deals.data.remote

import com.target.targetcasestudy.feature_deals.data.remote.model.deals.DealsApiResponse
import com.target.targetcasestudy.feature_deals.data.remote.model.deals.DealsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DealsAPI {
    @GET("deals")
    suspend fun getDealsList(): DealsApiResponse

    @GET("deals/{dealId}")
    suspend fun getDeal(@Path("dealId") dealId: String): DealsDto
}