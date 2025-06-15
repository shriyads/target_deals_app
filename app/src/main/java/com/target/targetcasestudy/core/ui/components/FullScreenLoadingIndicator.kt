package com.target.targetcasestudy.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.target.targetcasestudy.core.ui.theme.PrimaryRed

/**
 * A reusable composable that displays a full-screen circular loading indicator.
 * This is intended to be used as a default loading state for screens.
 */
@Composable
fun FullScreenLoadingIndicator(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    indicatorColor: Color = PrimaryRed
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = indicatorColor
        )
    }
}