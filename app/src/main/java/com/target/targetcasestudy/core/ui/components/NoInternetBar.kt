package com.target.targetcasestudy.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.target.targetcasestudy.core.ui.theme.Dimens
import com.target.targetcasestudy.core.ui.theme.Red
import com.target.targetcasestudy.core.ui.theme.RobotoFontFamily

/**
 * A composable bar to display a "No Internet Connectivity" message.
 * It has a red background and white text for high visibility.
 */
@Composable
fun NoInternetBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Red)
            .padding(vertical = Dimens.PaddingSmall),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No Internet Connectivity",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = Dimens.TextSmall,
                color = Color.White
            )
        )
    }
}