package com.target.targetcasestudy.feature_deals.data.remote.model.deals

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DealsApiResponse(
    @Json(name = "products")
    val deals: List<DealsDto>?
)