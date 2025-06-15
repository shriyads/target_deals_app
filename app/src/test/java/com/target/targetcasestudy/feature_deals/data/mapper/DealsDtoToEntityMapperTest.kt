package com.target.targetcasestudy.feature_deals.data.mapper

import com.google.common.truth.Truth.assertThat
import com.target.targetcasestudy.feature_deals.data.remote.model.deals.DealsDto
import com.target.targetcasestudy.feature_deals.data.remote.model.deals.PriceDto
import org.junit.Before
import org.junit.Test

class DealsDtoToEntityMapperTest {

    private lateinit var mapper: DealsDtoToEntityMapper

    @Before
    fun setUp() {
        mapper = DealsDtoToEntityMapper()
    }

    @Test
    fun `map should correctly convert DealsDto to DealsEntity with prices`() {

        val dto = DealsDto(
            id = 123,
            title = "Test Deal",
            description = "Great discount on electronics",
            aisle = "A1",
            salePrice = PriceDto(
                amountInCents = 1999,
                currencySymbol = "$",
                displayString = "$19.99"
            ),
            regularPrice = PriceDto(
                amountInCents = 2999,
                currencySymbol = "$",
                displayString = "$29.99"
            ),
            imageUrl = "http://example.com/image.png",
            availability = "In Stock",
            fulfillment = "Online"
        )

        // When
        val entity = mapper.map(dto)

        // Then
        assertThat(entity.id).isEqualTo(123)
        assertThat(entity.title).isEqualTo("Test Deal")
        assertThat(entity.description).isEqualTo("Great discount on electronics")
        assertThat(entity.aisle).isEqualTo("A1")
        assertThat(entity.salePrice).isEqualTo("$19.99")
        assertThat(entity.regularPrice).isEqualTo("$29.99")
        assertThat(entity.imageUrl).isEqualTo("http://example.com/image.png")
        assertThat(entity.availability).isEqualTo("In Stock")
        assertThat(entity.fulfillment).isEqualTo("Online")
    }

    @Test
    fun `map should handle null prices gracefully`() {
        // Given
        val dto = DealsDto(
            id = 456,
            title = "Another Deal",
            description = "Awesome TV item",
            aisle = "B2",
            salePrice = null,
            regularPrice = null,
            imageUrl = "http://example.com/item.png",
            availability = "Out of Stock",
            fulfillment = "In Store"
        )

        // When
        val entity = mapper.map(dto)

        // Then
        assertThat(entity.salePrice).isNull()
        assertThat(entity.regularPrice).isNull()
    }
}

