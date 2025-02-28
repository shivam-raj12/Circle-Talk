package com.shivam_raj.circletalk.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shivam_raj.circletalk.screens.auth.createAccount.CreateAccountScreen
import com.shivam_raj.circletalk.screens.auth.loginAccount.LoginAccountScreen
import com.shivam_raj.circletalk.screens.chat.chatScreen.ChatScreen
import com.shivam_raj.circletalk.screens.chat.mainScreen.MainScreen
import com.shivam_raj.circletalk.server.Server
import com.shivam_raj.circletalk.storage.CurrentUserManager
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

@Composable
fun NavGraph(
    onDataLoaded: () -> Unit
) {
    val navController = rememberNavController()
    var isUserLoggedIn by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        try {
            val ac = Server.getAccountInstance().get()
            Server.initializeUser(ac)
            isUserLoggedIn = true
            onDataLoaded()
        } catch (_: AppwriteException) {
            isUserLoggedIn = false
            onDataLoaded()
        } catch (_: Exception) {
            CurrentUserManager.getCurrentUserId(context).map {
                it != null
            }.collectLatest {
                isUserLoggedIn = it
                onDataLoaded()
            }
        }
    }
    if (isUserLoggedIn==null){
        CircularProgressIndicator()
        return
    }
    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn == true) Main else Auth
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
                MainScreen(navController)
            }
            animatedComposable<MainDestinations.ChatScreen> {
                val args = it.toRoute<MainDestinations.ChatScreen>()
                ChatScreen(navController, args.userId, args.userName, args.userProfile)
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