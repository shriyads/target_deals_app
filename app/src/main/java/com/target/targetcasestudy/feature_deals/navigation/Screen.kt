package com.target.targetcasestudy.feature_deals.navigation


sealed class Screen(val route: String) {
    object Splash : Screen(Routes.SPLASH)
    object DealsList : Screen(Routes.DEALS_LIST)
    object DealDetails : Screen("${Routes.DEAL_DETAILS}/{${RouteParams.DEAL_ID}}") {
        fun createRoute(dealId: String) = "${Routes.DEAL_DETAILS}/$dealId"
    }
}
