package com.shivam_raj.circletalk.screens.auth.loginAccount

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.shivam_raj.circletalk.network.AuthRequest
import com.shivam_raj.circletalk.screens.auth.AnimatedLoginButton
import com.shivam_raj.circletalk.screens.auth.FormInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun ContentForm(
    snackBarHostState: SnackbarHostState,
    onLoggedIn: () -> Unit
) {
    val viewModel = hiltViewModel<ContentFormViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val buttonEnabled by remember {
        derivedStateOf {
            viewModel.email.isNotBlank() &&
                    viewModel.password.isNotBlank() &&
                    Patterns.EMAIL_ADDRESS.matcher(viewModel.email).matches() &&
                    viewModel.password.length >= 8
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(0.85f),
    ) {
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
        AnimatedLoginButton(
            modifier = Modifier.fillMaxWidth(),
            isLoading = viewModel.isLoading,
            buttonText = "Login",
            enabled = buttonEnabled,
            onClick = {
                coroutineScope.launch {
                    val result = viewModel.loginToAccount()
                    if (result != null) {
                        snackBarHostState.showSnackbar(result)
                    } else {
                        onLoggedIn()
                    }
                }
            }
        )
    }
}

@HiltViewModel
class ContentFormViewModel @Inject constructor(private val authRequest: AuthRequest) : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set

    suspend fun loginToAccount(): String? {
        isLoading = true
        try {
            return authRequest.loginToAccount(
                email = email,
                password = password
            )
        } finally {
            isLoading = false
        }
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }
}