package com.example.movielog.features.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.movielog.features.auth.presentation.state.AuthState
import com.example.movielog.features.auth.presentation.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onSwitchToSignup: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var isSendingResetEmail by remember { mutableStateOf(false) }
    var resetMessage by remember { mutableStateOf<String?>(null) }
    var resetError by remember { mutableStateOf<String?>(null) }

    val state = viewModel.authState

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            onLoginSuccess()
        }
    }

    Scaffold { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                resetError = null
                                resetMessage = null
                            },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    passwordVisible = !passwordVisible
                                }) {
                                    Icon(
                                        imageVector = if (passwordVisible) {
                                            Icons.Default.VisibilityOff
                                        } else {
                                            Icons.Default.Visibility
                                        },
                                        contentDescription = if (passwordVisible) {
                                            "Hide Password"
                                        } else {
                                            "Show Password"
                                        }
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        TextButton(
                            onClick = { showForgotPasswordDialog = true },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Forgot password?")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.login(email, password)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = state !is AuthState.Loading
                        ) {
                            if (state is AuthState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Login")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = onSwitchToSignup,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Don't have an account? Sign Up")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    resetError != null -> {
                        Text(
                            text = resetError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    resetMessage != null -> {
                        Text(
                            text = resetMessage!!,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    state is AuthState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(
            email = email,
            isSending = isSendingResetEmail,
            onDismiss = { showForgotPasswordDialog = false },
            onConfirm = {
                if (email.isBlank()) {
                    resetError = "Enter your email address first."
                    showForgotPasswordDialog = false
                    return@ForgotPasswordDialog
                }

                isSendingResetEmail = true
                resetError = null
                resetMessage = null

                viewModel.sendPasswordResetEmail(
                    email = email,
                    onSuccess = {
                        isSendingResetEmail = false
                        showForgotPasswordDialog = false
                        resetMessage = "Password reset email sent. Use the link in your inbox to set a new password."
                    },
                    onFailure = { message ->
                        isSendingResetEmail = false
                        resetError = message
                    }
                )
            }
        )
    }
}

@Composable
private fun ForgotPasswordDialog(
    email: String,
    isSending: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (!isSending) onDismiss()
        },
        title = {
            Text("Forgot password")
        },
        text = {
            Text(
                text = if (email.isBlank()) {
                    "Enter your account email on the login screen first. We will send a password reset email there."
                } else {
                    "A password reset email will be sent to $email. Use the link in that email to set your new password."
                },
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isSending
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Send email")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSending
            ) {
                Text("Cancel")
            }
        }
    )
}
