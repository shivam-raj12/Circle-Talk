package com.shivam_raj.circletalk.screens.auth.createAccount

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.shivam_raj.circletalk.network.AuthRequest
import com.shivam_raj.circletalk.screens.AnimatedAlertDialog
import com.shivam_raj.circletalk.screens.auth.FormInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentForm(
    snackBarHostState: SnackbarHostState,
    onAccountCreated: () -> Unit
) {
    val viewModel = hiltViewModel<ContentFormViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val buttonEnabled by remember {
        derivedStateOf {
            viewModel.username.isNotBlank() &&
                    viewModel.email.isNotBlank() &&
                    viewModel.password.isNotBlank() &&
                    viewModel.usernameFieldError.isBlank() &&
                    Patterns.EMAIL_ADDRESS.matcher(viewModel.email).matches() &&
                    viewModel.password.length >= 8
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(0.85f),
    ) {
        FormInput(
            value = viewModel.username,
            onValueChange = viewModel::onUsernameChange,
            label = "Username",
            leadingIcon = Icons.Default.Person,
            enabled = !viewModel.isLoading,
            supportingText = viewModel.usernameFieldError
        )
        FormInput(
            value = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            label = "Email",
            leadingIcon = Icons.Default.Email,
            enabled = !viewModel.isLoading
        )
        FormInput(
            value = viewModel.password,
            onValueChange = viewModel::onPasswordChange,
            label = "Password(8+ character)",
            leadingIcon = Icons.Default.Lock,
            enabled = !viewModel.isLoading
        )
        Spacer(Modifier.height(4.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            enabled = buttonEnabled,
            onClick = {
                coroutineScope.launch {
                    val result = viewModel.createAccount()
                    if (result != null) {
                        snackBarHostState.showSnackbar(result)
                    } else {
                        onAccountCreated()
                    }
                }
            }) {
            Text(text = "Create Account")
        }
    }
    AnimatedAlertDialog(
        visible = viewModel.isLoading,
        dialogProperties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@HiltViewModel
class ContentFormViewModel @Inject constructor(private val authRequest: AuthRequest) : ViewModel() {
    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var usernameFieldError by mutableStateOf("")
        private set

    suspend fun createAccount(): String? {
        isLoading = true
        try {
            return authRequest.createUserAccount(
                username = username,
                email = email,
                password = password,
                name = username
            )
        } finally {
            isLoading = false
        }
    }

    fun onUsernameChange(username: String) {
        this.username = username
        usernameFieldError = validateUsername(username)
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    private fun validateUsername(username: String, maxLength: Int = 36): String {
        return when {
            username.isBlank() -> ""
            username.length > maxLength -> "Username must not exceed $maxLength characters."
            !username.first().isLetterOrDigit() -> "Username must start with a letter or number."
            username.any { !it.isLetterOrDigit() && it !in listOf('.', '-', '_') } ->
                "Username can only contain letters, numbers, periods (.), hyphens (-), and underscores (_)."

            else -> ""
        }
    }
}