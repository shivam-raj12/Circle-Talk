package com.shivam_raj.circletalk.screens.auth.createAccount

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

@Composable
fun CreateAccountScreen(
    navController: NavController
) {
    val snackBarHostState = remember { SnackbarHostState() }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        AnimatedHeader(
            text = "Create Your\nAccount:)",
            textColor = MaterialTheme.colorScheme.primary,
            waveColor = MaterialTheme.colorScheme.primaryContainer
        )
        ContentForm(
            snackBarHostState = snackBarHostState,
            onAccountCreated = {
                navController.navigate(Main){
                    popUpTo(AuthDestinations.LoginScreen){
                        inclusive = true
                    }
                }
            }
        )
    }
    BottomDesignedButton(
        modifier = Modifier.fillMaxSize(),
        text = buildAnnotatedString {
            append("Already have an account? ")
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Login")
            }
        },
        designColor = MaterialTheme.colorScheme.primaryContainer,
        onClick = {
            navController.navigateUp()
        }
    )
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