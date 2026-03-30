package com.example.movielog.features.auth.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.features.auth.presentation.state.AuthState
import com.example.movielog.features.auth.presentation.viewmodel.AuthViewModel

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onSwitchToLogin: () -> Unit,
    onSignupSuccess: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state = viewModel.authState

    // 🔥 Handle navigation on success
    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            onSignupSuccess()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(text = "Sign Up") // ✅ fixed

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.register(email, password)
        }) {
            Text("Sign Up") // ✅ fixed
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔁 Switch to Login
        TextButton(onClick = onSwitchToLogin) {
            Text("Already have an account? Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is AuthState.Loading -> Text("Loading...")
            is AuthState.Error -> Text(state.message)
            else -> {}
        }
    }
}