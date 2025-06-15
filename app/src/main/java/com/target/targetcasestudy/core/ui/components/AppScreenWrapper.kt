package com.target.targetcasestudy.core.ui.components
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.target.targetcasestudy.R
import com.target.targetcasestudy.core.ui.theme.PrimaryRed
import com.target.targetcasestudy.core.utils.network.NetworkConnectivityObserver
import com.target.targetcasestudy.core.utils.network.NetworkStatus
import com.target.targetcasestudy.feature_deals.navigation.Screen



/**
 * A top-level composable wrapper that provides a consistent app scaffold,
 * including a dynamic TopAppBar and a network connectivity bar.
 */
@Composable
fun AppScreenWrapper(
    navController: NavHostController,
    networkConnectivityObserver: NetworkConnectivityObserver,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val context = LocalContext.current

    // Observe the network status
    val networkStatus by networkConnectivityObserver.observe()
        .collectAsStateWithLifecycle(initialValue = NetworkStatus.Available)

    var previousStatus by remember { mutableStateOf<NetworkStatus?>(null) }

    LaunchedEffect(networkStatus) {
        if (previousStatus == NetworkStatus.Unavailable && networkStatus == NetworkStatus.Available) {
            Toast.makeText(context, "Internet connection restored.", Toast.LENGTH_SHORT).show()
        }
        previousStatus = networkStatus
    }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val route = currentBackStackEntry?.destination?.route

    val showBackButton = route?.startsWith(Screen.DealDetails.route) == true

    val topBarTitle = when {
        route?.startsWith(Screen.DealDetails.route) == true -> stringResource(R.string.deals_details_title)
        route == Screen.DealsList.route -> stringResource(R.string.deals_list_title)
        route == Screen.Splash.route -> ""
        else -> stringResource(R.string.deals_list_title)
    }

    Scaffold(
        topBar = {
            if (route != Screen.Splash.route) {
                Column {
                    TopAppBar(
                        title = { Text(topBarTitle) },
                        backgroundColor = Color.White,
                        navigationIcon = if (showBackButton) {
                            {
                                IconButton(onClick = {
                                    if (navController.previousBackStackEntry != null) {
                                        navController.popBackStack()
                                    } else {
                                        navController.navigate(Screen.DealsList.route) {
                                            popUpTo(Screen.DealsList.route) { inclusive = true }
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = PrimaryRed
                                    )
                                }
                            }
                        } else null
                    )
                    AnimatedVisibility(
                        visible = networkStatus == NetworkStatus.Unavailable,
                        enter = slideInVertically(initialOffsetY = { -it }),
                        exit = slideOutVertically(targetOffsetY = { -it })
                    ) {
                        NoInternetBar()
                    }
                }
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
