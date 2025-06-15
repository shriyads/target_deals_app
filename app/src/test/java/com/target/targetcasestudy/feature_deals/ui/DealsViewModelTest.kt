package com.target.targetcasestudy.feature_deals.ui

import com.target.targetcasestudy.core.ui.utils.UIState
import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.feature_deals.domain.usecase.GetDealsUseCase
import com.target.targetcasestudy.feature_deals.domain.usecase.SearchDealsUseCase
import com.target.targetcasestudy.feature_deals.ui.dealslist.DealsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class DealsViewModelTest {

    private lateinit var viewModel: DealsViewModel
    private lateinit var getDealsUseCase: GetDealsUseCase
    private lateinit var searchDealsUseCase: SearchDealsUseCase

    private val testDispatcher = StandardTestDispatcher()

    private val fakeDeals = listOf(
        Deals(
            id = 1,
            title = "Vizio",
            description = "Great deal!",
            salePrice = "$99.99",
            regularPrice = "$129.99",
            aisle = "A3",
            imageUrl = "http://image.com/img.png",
            fulfillment = "In Store",
            availability = "In Stock"
        ),
        Deals(
            id = 2,
            title = "Samsung",
            description = "Great deal 2!",
            salePrice = "$99.99",
            regularPrice = "$129.99",
            aisle = "A4",
            imageUrl = "http://image.com/img.png",
            fulfillment = "In Store",
            availability = "In Stock"
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getDealsUseCase = mockk()
        searchDealsUseCase = mockk()

        // Default mock setup
        coEvery { getDealsUseCase(Unit) } returns flowOf(APIResult.Success(fakeDeals))
        coEvery { searchDealsUseCase(any()) } returns flowOf(emptyList())

        viewModel = DealsViewModel(getDealsUseCase, searchDealsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchDeals emits UIState Success with data`() = runTest {
        advanceUntilIdle()

        val state = viewModel.dealsState.value
        assertTrue(state is UIState.Success)
        assertEquals(fakeDeals, (state as UIState.Success).data)
    }

    @Test
    fun `onSearchQueryChange triggers search and updates filteredDeals`() = runTest {
        val query = "Samsung"
        val expectedResult = listOf(fakeDeals[0])

        coEvery { searchDealsUseCase(query) } returns flowOf(expectedResult)

        viewModel.onSearchQueryChange(query)

        advanceTimeBy(400) // Allow debounce time
        advanceUntilIdle()

        assertEquals(expectedResult, viewModel.filteredDeals.value)
    }

    @Test
    fun `refreshDeals triggers isRefreshing true and then false`() = runTest {
        coEvery { getDealsUseCase(Unit) } returns flowOf(APIResult.Loading, APIResult.Success(fakeDeals))

        viewModel.refreshDeals()

        advanceUntilIdle()

        // should end in false after refresh completes
        assertEquals(false, viewModel.isRefreshing.value)
    }
}

