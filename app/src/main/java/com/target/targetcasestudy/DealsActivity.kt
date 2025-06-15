package com.target.targetcasestudy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding // Ensure this import is present
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.target.targetcasestudy.core.ui.base.BaseActivity
import com.target.targetcasestudy.core.ui.theme.MyAppTheme
import com.target.targetcasestudy.feature_splash_screen.ui.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.target.targetcasestudy.core.ui.components.AppScreenWrapper // Import the new wrapper
import com.target.targetcasestudy.core.utils.network.NetworkConnectivityObserver
import com.target.targetcasestudy.feature_deals.dealslistofflineworker.DealsSyncManager
import com.target.targetcasestudy.feature_deals.navigation.AppNavGraph
import com.target.targetcasestudy.feature_deals.navigation.Screen

@AndroidEntryPoint
class DealsActivity : BaseActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    @Inject
    lateinit var dealsSyncManager: DealsSyncManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { splashViewModel.isLoading.value }

        setContent {
            val navController = rememberNavController()

            // Observe the isLoading state from SplashViewModel for initial navigation decision.
            val isLoading by splashViewModel.isLoading.collectAsStateWithLifecycle()
            val startDestination = if (isLoading) Screen.Splash.route else Screen.DealsList.route

            MyAppTheme {
                AppScreenWrapper(
                    navController = navController,
                    networkConnectivityObserver = networkConnectivityObserver,
                ) { paddingValues ->
                    // This is the content lambda for AppScreenWrapper.
                    // It will contain your main navigation graph.
                    AppNavGraph(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(paddingValues) // Apply padding from Scaffold
                    )
                }
            }
        }
    }

    override fun onActivityStarted() {
        super.onActivityStarted()
        dealsSyncManager.scheduleDealsListSync()
    }
}