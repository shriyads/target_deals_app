package com.target.targetcasestudy.feature_deals.data.remote.model.deals

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DealsDto(
    val id: Int?,
    val title: String?,
    val availability: String?,
    val fulfillment:String?,
    @Json(name = "image_url")
    val imageUrl: String?,
    val aisle: String?,
    val description: String?,
    @Json(name = "sale_price")
    val salePrice: PriceDto?,
    @Json(name = "regular_price")
    val regularPrice: PriceDto?
)
