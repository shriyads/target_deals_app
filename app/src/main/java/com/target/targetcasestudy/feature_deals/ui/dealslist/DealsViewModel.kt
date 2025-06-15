package com.target.targetcasestudy.feature_deals.ui.dealslist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.target.targetcasestudy.core.ui.utils.UIState
import com.target.targetcasestudy.core.ui.utils.UiStateHolder
import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.feature_deals.domain.usecase.GetDealsUseCase
import com.target.targetcasestudy.feature_deals.domain.usecase.SearchDealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Deals List screen.
 * Manages fetching, searching, and refreshing of deals data.
 */
@HiltViewModel
class DealsViewModel @Inject constructor(
    private val getDealsUseCase: GetDealsUseCase,
    private val searchDealsUseCase: SearchDealsUseCase
) : ViewModel() {

    private val uiStateHolder = UiStateHolder<List<Deals>>()
    val dealsState: StateFlow<UIState<List<Deals>>> = uiStateHolder.uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filteredDeals = MutableStateFlow<List<Deals>>(emptyList())
    val filteredDeals: StateFlow<List<Deals>> = _filteredDeals

    // State to control the pull-to-refresh indicator
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        // Initial data fetch, not a user-initiated refresh
        fetchDeals(fromUserInitiatedRefresh = false)

        // Listen for query changes with debounce
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // 300ms delay to reduce unnecessary calls
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _filteredDeals.value = emptyList()
                    } else {
                        searchDealsUseCase(query).collectLatest {
                            _filteredDeals.value = it
                        }
                    }
                }
        }
    }

    /**
     * Updates the current search query.
     * @param newQuery The new search query string.
     */
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    /**
     * Fetches deals from the repository. This method is called both on initial load
     * and when a refresh is triggered.
     *
     * @param fromUserInitiatedRefresh If true, it indicates this fetch is from a user-triggered refresh
     * (e.g., pull-to-refresh) and should show the refreshing indicator.
     */
    private fun fetchDeals(fromUserInitiatedRefresh: Boolean) {
        viewModelScope.launch {
            // Set refreshing state to true ONLY if it's a user-initiated refresh
            if (fromUserInitiatedRefresh) {
                _isRefreshing.value = true
            }

            getDealsUseCase(Unit)
                .onEach { result ->
                    when (result) {
                        is APIResult.Loading -> {
                            // Only set full-screen loading if it's not a refresh
                            // and the screen isn't already showing success data.
                            // The pull-to-refresh indicator itself signifies loading during refresh.
                            if (!fromUserInitiatedRefresh && dealsState.value !is UIState.Success) {
                                uiStateHolder.setLoading()
                            }
                        }
                        is APIResult.Success -> {
                            uiStateHolder.setSuccess(result.data)
                            _isRefreshing.value = false // Set refreshing to false on success
                        }
                        is APIResult.Error -> {
                            uiStateHolder.setError(result.exception, result.apiMessage)
                            _isRefreshing.value = false // Set refreshing to false on error
                        }
                    }
                }
                .launchIn(this) // Launch the flow collection within the viewModelScope
        }
    }

    fun refreshDeals() {
        // Call fetchDeals with true to indicate a user-initiated refresh
        fetchDeals(fromUserInitiatedRefresh = true)
    }
}