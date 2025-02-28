package com.shivam_raj.circletalk.screens.auth.loginAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shivam_raj.circletalk.navigation.AuthDestinations
import com.shivam_raj.circletalk.navigation.Main
import com.shivam_raj.circletalk.screens.auth.AnimatedHeader
import com.shivam_raj.circletalk.screens.auth.BottomDesignedButton
import com.shivam_raj.circletalk.screens.auth.Direction

@Composable
fun LoginAccountScreen(
    navController: NavController
) {
    val snackBarHostState = remember { SnackbarHostState() }
    BottomDesignedButton(
        modifier = Modifier.fillMaxSize(),
        text = buildAnnotatedString {
            append("Don't have an account? ")
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Create Account")
            }
        },
        designColor = MaterialTheme.colorScheme.tertiaryContainer,
        direction = Direction.RIGHT,
        onClick = {
            navController.navigate(AuthDestinations.CreateAccountScreen)
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        AnimatedHeader(
            text = "Good to see you again!",
            textColor = MaterialTheme.colorScheme.tertiary,
            waveColor = MaterialTheme.colorScheme.tertiaryContainer
        )
        ContentForm(
            snackBarHostState = snackBarHostState,
            onLoggedIn = {
                navController.navigate(Main){
                    popUpTo(AuthDestinations.LoginScreen){
                        inclusive = true
                    }
                }
            }
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(), contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(
            hostState = snackBarHostState,
        )
    }
}