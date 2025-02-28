package com.shivam_raj.circletalk.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FormInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    enabled: Boolean,
    supportingText: String = "",
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        enabled = enabled,
        singleLine = true,
        supportingText = {
            AnimatedVisibility(
                visible = supportingText.isNotEmpty()
            ) {
                Text(text = supportingText)
            }
        },
        isError = supportingText.isNotEmpty(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
            errorCursorColor = MaterialTheme.colorScheme.onErrorContainer,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorContainerColor = MaterialTheme.colorScheme.errorContainer,
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FormInput(
        value = "ih",
        onValueChange = {},
        label = "Username",
        leadingIcon = Icons.Default.Lock,
        enabled = true,
        supportingText = "Username must not exceed 36 characters."
    )
}