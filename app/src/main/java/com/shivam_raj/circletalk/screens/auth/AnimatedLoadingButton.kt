package com.shivam_raj.circletalk.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedLoginButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    buttonText: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    AnimatedContent(
        targetState = isLoading,
        label = "",
        transitionSpec = {
            fadeIn(tween(1000)) + scaleIn(tween(1000)) togetherWith fadeOut(tween(1000)) + scaleOut(tween(1000))
        }
    ) {
        if (it) {
            Box(
                modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                        .padding(8.dp),
                    trackColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            Button(
                onClick = onClick,
                modifier = modifier,
                shape = MaterialTheme.shapes.medium,
                enabled = enabled
            ) {
                Text(text = buttonText)
            }
        }
    }
}