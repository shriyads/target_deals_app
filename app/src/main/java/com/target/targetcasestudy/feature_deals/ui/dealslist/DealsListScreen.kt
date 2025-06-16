package com.target.targetcasestudy.feature_deals.ui.dealslist
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.target.targetcasestudy.core.ui.components.ErrorScreen
import com.target.targetcasestudy.core.ui.theme.LightGray
import com.target.targetcasestudy.core.ui.theme.PrimaryRed
import com.target.targetcasestudy.core.ui.utils.UIState
import androidx.compose.foundation.background // Import for background modifier
import androidx.compose.ui.graphics.Color // Import for Color

// Imports for Compose Material Pull Refresh
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.foundation.layout.Box // Required for layering pull refresh indicator
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.pullrefresh.PullRefreshIndicator // Import PullRefreshIndicator
import androidx.compose.foundation.rememberScrollState // Import rememberScrollState
import androidx.compose.foundation.verticalScroll // Import verticalScroll
import androidx.compose.foundation.layout.fillMaxHeight // Import fillMaxHeight
import androidx.compose.ui.res.stringResource
import com.target.targetcasestudy.R
import com.target.targetcasestudy.core.ui.components.SearchBar


/**
 * Main composable screen for displaying the list of deals with search functionality.
 * This screen observes the ViewModel's state and delegates UI rendering to smaller,
 * more focused, and reusable composables.
 * It also now supports pull-to-refresh functionality using Compose Material's API.
 *
 * @param viewModel The ViewModel responsible for providing deals data.
 * @param onDealClick Callback to navigate to the deal details screen when an item is clicked.
 */
@OptIn(ExperimentalMaterialApi::class) // Opt-in for ExperimentalMaterialApi for pullRefresh
@Composable
fun DealsListScreen(
    viewModel: DealsViewModel = hiltViewModel(), // ViewModel provided by Hilt
    onDealClick: (String) -> Unit
) {
    // Observe UI state from the ViewModel
    val dealsState by viewModel.dealsState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredDeals by viewModel.filteredDeals.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle() // Observe refresh state

    // State for LazyColumn scrolling
    val listState = rememberLazyListState() // This is for DealsListContent's LazyColumn

    // Handle back button press to clear search query if active
    BackHandler(enabled = searchQuery.isNotEmpty()) {
        viewModel.onSearchQueryChange("")
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refreshDeals
    )

    val mainColumnScrollState = rememberScrollState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(mainColumnScrollState)
        ) {
            // Divider at the top
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .shadow(2.dp),
                color = LightGray
            )


            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                label = stringResource(R.string.search_deals),
                placeholder = "Search deals",
                onSpeechResult = { recognizedText ->
                    viewModel.onSearchQueryChange(recognizedText)
                }
            )

            // Display content based on the UIState
            when (dealsState) {
                is UIState.Loading -> {
                    CircularProgressIndicator(
                        color = PrimaryRed,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    )
                }
                is UIState.Error -> {
                    val errorState = dealsState as UIState.Error
                    ErrorScreen(
                        message = errorState.message ?: "Something went wrong loading deals.",
                        onRetry = { viewModel.refreshDeals() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    )
                }
                is UIState.Success -> {
                    val allDeals = (dealsState as UIState.Success).data
                    val dealsToDisplay = if (searchQuery.isBlank()) allDeals else filteredDeals
                    DealsListContent(
                        deals = dealsToDisplay,
                        searchQuery = searchQuery,
                        onDealClick = onDealClick,
                        listState = listState,
                        modifier = Modifier.weight(1f)
                    )
                }
                UIState.Idle -> {
                    Spacer(modifier = Modifier.fillMaxHeight())
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
            contentColor = PrimaryRed
        )
    }
}