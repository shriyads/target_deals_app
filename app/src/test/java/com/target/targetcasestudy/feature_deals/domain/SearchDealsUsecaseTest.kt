package com.target.targetcasestudy.feature_deals.domain

import com.google.common.truth.Truth.assertThat
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import com.target.targetcasestudy.feature_deals.domain.usecase.SearchDealsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchDealsUseCaseTest {

    private lateinit var repository: DealsRepository
    private lateinit var useCase: SearchDealsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchDealsUseCase(repository)
    }

    @Test
    fun `invoke should return Samsung deal when searched with samsung query`() = runTest {
        // Given
        val query = "samsung"
        val filteredDeals = listOf(
            Deals(
                id = 1,
                title = "Samsung TV",
                description = "A Samsung Tv 55 inch",
                aisle = "B1",
                salePrice = "$180.00",
                regularPrice = "$200.00",
                imageUrl = "http://example.com/samsungtv.jpg",
                availability = "In Stock",
                fulfillment = "Online"
            )
        )

        coEvery { repository.searchDeals(query) } returns flowOf(filteredDeals)

        // When
        val result = useCase(query)

        // Then
        result.collect { deals ->
            assertThat(deals).hasSize(1)
            val deal = deals.first()
            assertThat(deal.title).contains("Samsung")
            assertThat(deal.salePrice).isEqualTo("$180.00")
            assertThat(deal.regularPrice).isEqualTo("$200.00")
            assertThat(deal.imageUrl).isEqualTo("http://example.com/samsungtv.jpg")
        }
    }

    @Test
    fun `invoke should return empty list when no samsung deals are found`() = runTest {
        // Given
        val query = "samsung"
        coEvery { repository.searchDeals(query) } returns flowOf(emptyList())

        // When
        val result = useCase(query)

        // Then
        result.collect { deals ->
            assertThat(deals).isEmpty()
        }
    }
}
