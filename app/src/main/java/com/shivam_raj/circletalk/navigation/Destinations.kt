package com.shivam_raj.circletalk.navigation

import kotlinx.serialization.Serializable

sealed class MainDestinations {
    @Serializable
    object MainScreen: MainDestinations()
}

sealed class AuthDestinations {
    @Serializable
    object LoginScreen: AuthDestinations()

    @Serializable
    object CreateAccountScreen: AuthDestinations()

}

@Serializable
object Auth

@Serializable
object Main