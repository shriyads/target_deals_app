package com.target.targetcasestudy.feature_deals.data.mapper

import com.target.targetcasestudy.core.utils.mapper.Mapper
import com.target.targetcasestudy.feature_deals.data.local.entity.DealsEntity
import com.target.targetcasestudy.feature_deals.data.remote.model.deals.DealsDto
import javax.inject.Inject

class DealsDtoToEntityMapper @Inject constructor() : Mapper<DealsDto, DealsEntity> {
    override fun map(from: DealsDto): DealsEntity {
        return DealsEntity(
            id = from.id,
            title = from.title,
            description = from.description,
            aisle = from.aisle,
            salePrice = from.salePrice?.displayString,
            regularPrice = from.regularPrice?.displayString,
            imageUrl = from.imageUrl,
            availability = from.availability,
            fulfillment = from.fulfillment
        )
    }
}
