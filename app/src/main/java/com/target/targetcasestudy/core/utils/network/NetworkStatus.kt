package com.target.targetcasestudy.core.utils.network


/*
 * Sealed class representing the different states of network connectivity.
 */

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}