package com.example.movielog.features.auth.presentation.screen

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.features.auth.presentation.viewmodel.AuthViewModel
import com.example.movielog.features.auth.presentation.state.AuthState

@Composable
fun SignupScreen(viewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state = viewModel.authState

    Column(modifier = Modifier.padding(16.dp)) {

        Text(text = "Login")

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
            viewModel.register(email,password)
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is AuthState.Loading -> Text("Loading...")
            is AuthState.Error -> Text(state.message)
            is AuthState.Success -> Text("Welcome ${state.user.email}")
            else -> {}
        }
    }
}