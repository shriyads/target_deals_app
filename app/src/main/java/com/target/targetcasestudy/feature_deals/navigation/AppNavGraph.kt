package com.target.targetcasestudy.feature_deals.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.target.targetcasestudy.feature_deals.navigation.RouteParams.DEAL_ID
import com.target.targetcasestudy.feature_deals.ui.dealdetails.DealDetailsScreen
import com.target.targetcasestudy.feature_deals.ui.dealslist.DealsListScreen
import com.target.targetcasestudy.feature_splash_screen.ui.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.popBackStack()
                navController.navigate(Screen.DealsList.route)
            })
        }

        composable(Screen.DealsList.route) {
            DealsListScreen(
                onDealClick = { dealId ->
                    navController.navigate(Screen.DealDetails.createRoute(dealId))
                }
            )
        }

        composable(
            route = Screen.DealDetails.route,
            arguments = listOf(navArgument(DEAL_ID) { type = NavType.StringType })
        ) {
            DealDetailsScreen()
        }
    }
}
