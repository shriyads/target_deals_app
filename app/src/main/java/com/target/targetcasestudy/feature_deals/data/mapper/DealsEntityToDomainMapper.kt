package com.target.targetcasestudy.feature_deals.data.mapper

import com.target.targetcasestudy.core.utils.mapper.Mapper
import com.target.targetcasestudy.feature_deals.data.local.entity.DealsEntity
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import javax.inject.Inject

class DealsEntityToDomainMapper @Inject constructor() : Mapper<DealsEntity, Deals> {

    override fun map(from: DealsEntity): Deals {
        return Deals(
            id = from.id ?: 0,
            title = from.title ?: DEFAULT_TITLE,
            description = from.description ?: DEFAULT_DESCRIPTION,
            salePrice = from.salePrice ?: DEFAULT_SALE_PRICE,
            regularPrice = from.regularPrice ?: DEFAULT_REGULAR_PRICE,
            aisle = from.aisle ?: DEFAULT_AISLE,
            imageUrl = from.imageUrl ?: DEFAULT_IMAGE_URL,
            fulfillment = from.fulfillment ?: DEFAULT_FULFILLMENT,
            availability = from.availability ?: DEFAULT_AVAILABILITY
        )
    }

    companion object {
        private const val DEFAULT_TITLE = "No Title"
        private const val DEFAULT_DESCRIPTION = "No Description"
        private const val DEFAULT_SALE_PRICE = "$180.99"
        private const val DEFAULT_REGULAR_PRICE = ""
        private const val DEFAULT_AISLE = "No aisle"
        private const val DEFAULT_IMAGE_URL = "No Image"
        private const val DEFAULT_FULFILLMENT = "No fulfillment"
        private const val DEFAULT_AVAILABILITY = "No availability"
    }
}



