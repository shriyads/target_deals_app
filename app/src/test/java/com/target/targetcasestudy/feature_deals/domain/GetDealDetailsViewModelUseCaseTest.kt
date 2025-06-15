package com.target.targetcasestudy.feature_deals.domain

import com.google.common.truth.Truth.assertThat
import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.feature_deals.domain.repository.DealsRepository
import com.target.targetcasestudy.feature_deals.domain.usecase.GetDealDetailsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetDealDetailsViewModelUseCaseTest {

    private lateinit var repository: DealsRepository
    private lateinit var useCase: GetDealDetailsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetDealDetailsUseCase(repository)
    }

    @Test
    fun `invoke should return deal details successfully`() = runTest {
        // Given
        val dealId = "123"
        val deal = Deals(
            id = dealId.toInt(), // Convert string to int
            title = "Test Deal",
            description = "Test Description",
            aisle = "A1",
            salePrice = "$10.00",           // salePrice as string
            regularPrice = "$20.00",        // regularPrice as string
            imageUrl = "http://example.com/image.jpg",
            availability = "In Stock",
            fulfillment = "Online"
        )
        val expectedResult = APIResult.Success(deal)

        coEvery { repository.getDealDetails(dealId) } returns flowOf(expectedResult)

        // When
        val result = useCase(dealId)

        // Then
        result.collect { apiResult ->
            assertThat(apiResult).isInstanceOf(APIResult.Success::class.java)
            assertThat((apiResult as APIResult.Success).data).isEqualTo(deal)
        }
    }

    @Test
    fun `invoke should return APIResult Error when repository fails`() = runTest {
        // Given
        val dealId = "456"
        val expectedError = APIResult.Error(
            exception = RuntimeException("Something went wrong")
        )

        coEvery { repository.getDealDetails(dealId) } returns flowOf(expectedError)

        // When
        val result = useCase(dealId)

        // Then
        result.collect { apiResult ->
            assertThat(apiResult).isInstanceOf(APIResult.Error::class.java)
            assertThat((apiResult as APIResult.Error).exception.message).isEqualTo("Something went wrong")
        }
    }
}

