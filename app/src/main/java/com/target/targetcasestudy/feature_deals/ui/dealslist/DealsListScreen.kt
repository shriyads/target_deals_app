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
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredDeals by viewModel.filteredDeals.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle() // Observe refresh state

    // State for LazyColumn scrolling
    val listState = rememberLazyListState() // This is for DealsListContent's LazyColumn

    // Handle back button press to clear search query if active
    BackHandler(enabled = searchQuery.isNotEmpty()) {
        viewModel.onSearchQueryChange("")
    }

    // Create the pull refresh state. This should be outside the `when` to always be available.
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refreshDeals
    )

    // Remember a scroll state for the main content column to ensure it's always scrollable.
    // This allows pull-to-refresh even when content is short or an error screen is shown.
    val mainColumnScrollState = rememberScrollState()

    // Wrap the entire content with a Box and apply the pullRefresh modifier
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set the overall background color to white
            .pullRefresh(pullRefreshState) // Apply the pullRefresh modifier to the entire Box
    ) {
        // Main content column that is always scrollable.
        // The `verticalScroll` modifier here ensures that even if the visible content
        // (e.g., ErrorScreen or CircularProgressIndicator) is short, the Column itself
        // is scrollable, allowing the pull-to-refresh gesture to be detected.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(mainColumnScrollState) // Make this column always scrollable
        ) {
            // Divider at the top
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .shadow(2.dp),
                color = LightGray
            )

            // Search Bar (always visible, outside the `when` block)
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
                    // Center the loading indicator within the remaining space of the Column,
                    // making sure it fills vertically to allow scrolling for PTR.
                    CircularProgressIndicator(
                        color = PrimaryRed,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight() // Fill max height to make the column scrollable
                            .wrapContentSize(Alignment.Center)
                    )
                }
                is UIState.Error -> {
                    val errorState = dealsState as UIState.Error
                    // Ensure the error screen fills the remaining vertical space,
                    // making the column scrollable for PTR.
                    ErrorScreen(
                        message = errorState.message ?: "Something went wrong loading deals.",
                        onRetry = { viewModel.refreshDeals() }, // Provide retry action
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight() // Fill max height to make the column scrollable
                            .wrapContentSize(Alignment.Center) // Center its content
                    )
                }
                is UIState.Success -> {
                    val allDeals = (dealsState as UIState.Success).data
                    val dealsToDisplay = if (searchQuery.isBlank()) allDeals else filteredDeals

                    // Delegate the rendering of the list content.
                    // This will naturally be scrollable via its LazyColumn.
                    DealsListContent(
                        deals = dealsToDisplay,
                        searchQuery = searchQuery,
                        onDealClick = onDealClick,
                        listState = listState,
                        modifier = Modifier.weight(1f) // Make the list fill remaining space
                    )
                }
                UIState.Idle -> {
                    // Initial state, ensure there's enough height to enable pull-to-refresh.
                    // A Spacer filling the remaining height makes the Column scrollable.
                    Spacer(modifier = Modifier.fillMaxHeight())
                }
            }
        }

        // Place the PullRefreshIndicator on top, aligned centrally, always within the Box.
        PullRefreshIndicator(
            refreshing = isRefreshing, // Controls the visibility and animation
            state = pullRefreshState, // Linked to the pull refresh state
            modifier = Modifier.align(Alignment.TopCenter), // Align to the top center of the Box
            backgroundColor = Color.White, // Background color of the indicator
            contentColor = PrimaryRed // Color of the rotating indicator
        )
    }
}