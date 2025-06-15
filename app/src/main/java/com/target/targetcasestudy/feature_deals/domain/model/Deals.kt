package com.target.targetcasestudy.feature_deals.domain.model

data class Deals(
  val id: Int,
  val title: String,
  val availability: String,
  val fulfillment:String,
  val imageUrl: String,
  val aisle: String,
  val description: String,
  val salePrice: String,
  val regularPrice: String,

)