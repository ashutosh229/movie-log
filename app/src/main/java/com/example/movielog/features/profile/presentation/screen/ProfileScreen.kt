package com.example.movielog.features.profile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.core.ui.theme.ThemeViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    themeViewModel: ThemeViewModel
) {

    val isDarkMode by themeViewModel.isDarkMode.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode")

            Switch(
                checked = isDarkMode,
                onCheckedChange = {
                    themeViewModel.setDarkMode(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onLogout) {
            Text("Logout")
        }
    }
}