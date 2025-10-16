package com.anz.feature.users.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.anz.feature.users.presentation.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersFeatureRoot(vm: UsersViewModel) {
    val selectedUser by vm.selectedUser.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Users", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0), // Toolbar background color
                    titleContentColor = Color.White,     // Title text color
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (selectedUser == null) {
                UsersListScreen(vm)
            } else {
                selectedUser?.let {
                    UserDetailScreen(user = it, vm = vm)
                }
                
            }
        }
    }
}