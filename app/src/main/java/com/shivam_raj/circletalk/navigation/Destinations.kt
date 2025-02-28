package com.shivam_raj.circletalk.navigation

import kotlinx.serialization.Serializable

sealed class MainDestinations {
    @Serializable
    object MainScreen: MainDestinations()

    @Serializable
    data class ChatScreen(
        val userId: String,
        val userName: String,
        val userProfile: String?
    ): MainDestinations()
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