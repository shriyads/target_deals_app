package com.target.targetcasestudy.core.ui.utils

import androidx.compose.runtime.Composable
import com.target.targetcasestudy.core.ui.components.ErrorScreen
import com.target.targetcasestudy.core.ui.components.FullScreenLoadingIndicator


@Composable
    fun <T> UIState<T>.RenderDefaultUI(
        onRetry: () -> Unit = {},
        onSuccess: @Composable (T) -> Unit
    ) {
        when (this) {
            is UIState.Loading -> FullScreenLoadingIndicator()
            is UIState.Error -> ErrorScreen(message ?: "Unknown error", onRetry)
            is UIState.Success -> onSuccess(data)
            UIState.Idle -> {}
        }
    }

