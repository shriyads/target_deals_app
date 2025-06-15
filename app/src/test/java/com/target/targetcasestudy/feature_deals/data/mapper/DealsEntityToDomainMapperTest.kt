package com.target.targetcasestudy.feature_deals.data.mapper


import com.google.common.truth.Truth.assertThat
import com.target.targetcasestudy.feature_deals.data.local.entity.DealsEntity
import org.junit.Test

class DealsEntityToDomainMapperTest {

    private val mapper = DealsEntityToDomainMapper()

    @Test
    fun `map should return correctly mapped Deals when all fields are non-null`() {
        val entity = DealsEntity(
            id = 123,
            title = "Sample Deal",
            description = "Great deal!",
            salePrice = "$99.99",
            regularPrice = "$129.99",
            aisle = "A3",
            imageUrl = "http://image.com/img.png",
            fulfillment = "In Store",
            availability = "In Stock"
        )

        val result = mapper.map(entity)

        assertThat(result.id).isEqualTo(123)
        assertThat(result.title).isEqualTo("Sample Deal")
        assertThat(result.description).isEqualTo("Great deal!")
        assertThat(result.salePrice).isEqualTo("$99.99")
        assertThat(result.regularPrice).isEqualTo("$129.99")
        assertThat(result.aisle).isEqualTo("A3")
        assertThat(result.imageUrl).isEqualTo("http://image.com/img.png")
        assertThat(result.fulfillment).isEqualTo("In Store")
        assertThat(result.availability).isEqualTo("In Stock")
    }

    @Test
    fun `map should apply fallback values when fields are null`() {
        val entity = DealsEntity(
            id = null,
            title = null,
            description = null,
            salePrice = null,
            regularPrice = null,
            aisle = null,
            imageUrl = null,
            fulfillment = null,
            availability = null
        )

        val result = mapper.map(entity)

        assertThat(result.id).isEqualTo(0)
        assertThat(result.title).isEqualTo("No Title")
        assertThat(result.description).isEqualTo("No Description")
        assertThat(result.salePrice).isEqualTo("$180.99")
        assertThat(result.regularPrice).isEqualTo("")
        assertThat(result.aisle).isEqualTo("No aisle")
        assertThat(result.imageUrl).isEqualTo("No Image")
        assertThat(result.fulfillment).isEqualTo("No fulfillment")
        assertThat(result.availability).isEqualTo("No availability")
    }
}
