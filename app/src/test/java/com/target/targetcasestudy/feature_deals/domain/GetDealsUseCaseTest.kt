package com.target.targetcasestudy.feature_deals.domain

import com.google.common.truth.Truth.assertThat
import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import com.target.targetcasestudy.feature_deals.domain.usecase.GetDealsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class GetDealsUseCaseTest {

    private lateinit var repository: DealsRepository
    private lateinit var useCase: GetDealsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetDealsUseCase(repository)
    }

    @Test
    fun `invoke should return list of deals successfully`() = runTest {
        // Given
        val deals = listOf(
            Deals(
                id = 1,
                title = "Deal 1",
                description = "Desc 1",
                aisle = "A1",
                salePrice = "$10.00",
                regularPrice = "$15.00",
                imageUrl = "http://example.com/1.jpg",
                availability = "In Stock",
                fulfillment = "Online"
            ),
            Deals(
                id = 2,
                title = "Deal 2",
                description = "Desc 2",
                aisle = "A2",
                salePrice = "$20.00",
                regularPrice = "$30.00",
                imageUrl = "http://example.com/2.jpg",
                availability = "Out of Stock",
                fulfillment = "In Store"
            )
        )

        val expectedResult = APIResult.Success(deals)

        coEvery { repository.getDeals() } returns flowOf(expectedResult)

        // When
        val result = useCase(Unit)

        // Then
        result.collect { apiResult ->
            assertThat(apiResult).isInstanceOf(APIResult.Success::class.java)
            assertThat((apiResult as APIResult.Success).data).isEqualTo(deals)
        }
    }

    @Test
    fun `invoke should return APIResult Error when repository fails`() = runTest {
        // Given
        val expectedError = APIResult.Error(
            exception = RuntimeException("Failed to fetch deals")
        )

        coEvery { repository.getDeals() } returns flowOf(expectedError)

        // When
        val result = useCase(Unit)

        // Then
        result.collect { apiResult ->
            assertThat(apiResult).isInstanceOf(APIResult.Error::class.java)
            assertThat((apiResult as APIResult.Error).exception.message).isEqualTo("Failed to fetch deals")
        }
    }
}
