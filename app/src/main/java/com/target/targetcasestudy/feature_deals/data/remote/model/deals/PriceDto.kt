package com.target.targetcasestudy.feature_deals.data.remote.model.deals

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PriceDto(
    @Json(name = "amount_in_cents")
    val amountInCents: Int?,
    @Json(name = "currency_symbol")
    val currencySymbol: String?,
    @Json(name = "display_string")
    val displayString: String?
)