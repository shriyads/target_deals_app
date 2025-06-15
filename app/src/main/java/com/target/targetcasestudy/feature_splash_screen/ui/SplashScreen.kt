package com.target.targetcasestudy.feature_splash_screen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.target.targetcasestudy.R
import com.target.targetcasestudy.core.ui.theme.PrimaryRed

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onSplashFinished: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    // When isLoading becomes false, trigger the navigation callback.
    // This ensures the splash screen transitions to the next screen only after
    // any initial loading (simulated by the ViewModel) is complete.
    LaunchedEffect(isLoading) {
        if (!isLoading) {
            onSplashFinished()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryRed),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.target_icon),
            contentDescription = "Target Logo",
            modifier = Modifier.size(120.dp)
        )
    }
}