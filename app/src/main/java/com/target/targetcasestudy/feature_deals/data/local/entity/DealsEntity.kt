package com.target.targetcasestudy.feature_deals.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deals")
data class DealsEntity(
    @PrimaryKey
    val id: Int?,
    val title: String?,
    val availability: String?,
    val fulfillment:String?,
    val imageUrl: String?,
    val aisle: String?,
    val description: String?,
    val salePrice :String?,
    val regularPrice: String?,

)