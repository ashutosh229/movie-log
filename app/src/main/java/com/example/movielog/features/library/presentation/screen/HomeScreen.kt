package com.example.movielog.features.library.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//TODO: Migrate the home screen to core UI
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToShow: () -> Unit
) {

    Column(modifier = Modifier.padding(16.dp)) {

        Button(
            onClick = onNavigateToSearch,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search Content")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToShow,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Show Entries")
        }
    }
}