package com.target.targetcasestudy.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.target.targetcasestudy.core.ui.theme.Dimens
import com.target.targetcasestudy.core.ui.theme.RobotoFontFamily

/**
 * A reusable composable to display an error message, optionally with a retry button.
 */
@Composable
fun ErrorScreen(
    message: String,
    onRetry: (() -> Unit)? = null, // Nullable to make retry optional
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingMedium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $message",
            textAlign = TextAlign.Center,
            fontFamily = RobotoFontFamily
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        onRetry?.let {
            Button(onClick = it) {
                Text("Retry", fontFamily = RobotoFontFamily)
            }
        }
    }
}
