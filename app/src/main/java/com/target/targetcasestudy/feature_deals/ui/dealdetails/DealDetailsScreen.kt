package com.target.targetcasestudy.feature_deals.ui.dealdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.target.targetcasestudy.R
import com.target.targetcasestudy.core.ui.components.ErrorScreen
import com.target.targetcasestudy.core.ui.theme.PrimaryRed
import com.target.targetcasestudy.core.ui.utils.RenderDefaultUI
import com.target.targetcasestudy.core.ui.utils.UIState
import com.target.targetcasestudy.feature_deals.domain.model.Deals

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DealDetailsScreen(
    viewModel: DealDetailsViewModel = hiltViewModel()
) {
    val dealState by viewModel.dealDetailsState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refreshDealDetails
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pullRefresh(pullRefreshState)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            dealState.RenderDefaultUI(
                onRetry = viewModel::refreshDealDetails,
                onSuccess = { deal ->
                    if (deal != null) {
                        DealDetailsContent(
                            deal = deal,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        ErrorScreen(
                            message = stringResource(id = R.string.deal_Details_not_found_error),
                            onRetry = viewModel::refreshDealDetails
                        )
                    }
                }
            )
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
