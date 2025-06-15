package com.target.targetcasestudy.feature_deals.ui.dealdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.target.targetcasestudy.core.ui.utils.UIState
import com.target.targetcasestudy.core.ui.utils.UiStateHolder
import com.target.targetcasestudy.core.utils.apiresult.APIResult
import com.target.targetcasestudy.feature_deals.domain.model.Deals
import com.target.targetcasestudy.feature_deals.domain.usecase.GetDealDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Deal Details screen.
 * This ViewModel is responsible for fetching and managing the UI state for a single deal's details.
 * It uses [GetDealUseCase] to retrieve deal data from the domain layer.
*/
@HiltViewModel
class DealDetailsViewModel @Inject constructor(
    private val getDealUseCase: GetDealDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val uiStateHolder = UiStateHolder<Deals?>()
    val dealDetailsState: StateFlow<UIState<Deals?>> = uiStateHolder.uiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val dealId: String? = savedStateHandle[KEY_DEAL_ID]

    init {
        if (dealId != null) {
            fetchDealDetails(dealId)
        } else {
            uiStateHolder.setError(
                IllegalArgumentException(EXCEPTION_MESSAGE_DEAL_ID_MISSING),
                USER_ERROR_MESSAGE_DEAL_ID_MISSING
            )
        }
    }

    private fun fetchDealDetails(dealId: String) {
        // Avoid re-fetching if already loaded successfully
        val currentState = dealDetailsState.value
        if (currentState is UIState.Success && currentState.data != null) return

        viewModelScope.launch {
            uiStateHolder.setLoading() // Explicitly set loading

            getDealUseCase(dealId)
                .onEach { result ->
                    when (result) {
                        is APIResult.Loading -> {} // Already handled
                        is APIResult.Success -> uiStateHolder.setSuccess(result.data)
                        is APIResult.Error -> uiStateHolder.setError(result.exception, result.apiMessage)
                    }
                }
                .launchIn(this)
        }
    }

    fun refreshDealDetails() {
        dealId?.let {
            viewModelScope.launch {
                _isRefreshing.emit(true)
                uiStateHolder.setLoading() // Set loading explicitly

                getDealUseCase(it)
                    .onEach { result ->
                        when (result) {
                            is APIResult.Loading -> {} // Already handled
                            is APIResult.Success -> uiStateHolder.setSuccess(result.data)
                            is APIResult.Error -> uiStateHolder.setError(result.exception, result.apiMessage)
                        }
                    }
                    .launchIn(this)

                _isRefreshing.emit(false)
            }
        }
    }

    companion object {
        const val KEY_DEAL_ID = "dealId"
        const val EXCEPTION_MESSAGE_DEAL_ID_MISSING = "Deal ID is missing."
        const val USER_ERROR_MESSAGE_DEAL_ID_MISSING = "Missing deal information."
    }
}
