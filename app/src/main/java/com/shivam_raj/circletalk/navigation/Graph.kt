package com.shivam_raj.circletalk.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.shivam_raj.circletalk.screens.auth.createAccount.CreateAccountScreen
import com.shivam_raj.circletalk.screens.auth.loginAccount.LoginAccountScreen
import com.shivam_raj.circletalk.screens.chat.mainScreen.MainScreen
import com.shivam_raj.circletalk.storage.CurrentUserManager

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val currentUser = CurrentUserManager.getCurrentUserId(LocalContext.current).collectAsStateWithLifecycle(null).value
    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) Auth else Main
    ) {
        navigation<Auth>(
            startDestination = AuthDestinations.LoginScreen,
        ) {
            animatedComposable<AuthDestinations.CreateAccountScreen> {
                CreateAccountScreen(navController)
            }
            animatedComposable<AuthDestinations.LoginScreen> {
                LoginAccountScreen(navController)
            }
        }
        navigation<Main>(
            startDestination = MainDestinations.MainScreen
        ) {
            animatedComposable<MainDestinations.MainScreen> {
                MainScreen()
            }
        }
    }
}

inline fun <reified T : Any> NavGraphBuilder.animatedComposable(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = T::class,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        content = content
    )
}