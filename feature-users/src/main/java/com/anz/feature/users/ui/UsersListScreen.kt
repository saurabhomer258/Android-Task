package com.anz.feature.users.ui

import android.graphics.pdf.models.ListItem
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon


import androidx.compose.runtime.*

import androidx.compose.ui.Modifier

import com.anz.feature.users.presentation.UsersViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anz.feature.users.presentation.UsersUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(vm: UsersViewModel) {
    val uiState by vm.state.collectAsState()
    val users by vm.users.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // ensure users flow is collected (populates _users)
    LaunchedEffect(Unit) {
        vm.loadUsers()
    }
    
    // show transient snackbar when an error occurs but users are already present
    LaunchedEffect(uiState) {
        if (uiState is UsersUiState.Error && users.isNotEmpty()) {
            val msg = (uiState as UsersUiState.Error).message
            snackbarHostState.showSnackbar(msg)
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            when {
                // 1) Loading state and no users yet -> full screen loader
                uiState is UsersUiState.Loading && users.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Loading users…", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                
                // 2) Error and no users -> full screen error with retry
                uiState is UsersUiState.Error && users.isEmpty() -> {
                    val message = (uiState as UsersUiState.Error).message
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            Button(onClick = {
                                vm.refresh()
                                vm.loadUsers()
                            }) {
                                Text("Retry")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(onClick = { /* optional fallback */ }) {
                                Text("Cancel")
                            }
                        }
                    }
                }
                
                // 3) Otherwise: show users list (may include transient snackbar for later errors)
                else -> {
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(users) { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { vm.selectUser(user) },
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = user.name.firstOrNull()?.uppercase() ?: "?",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.width(12.dp))
                                    
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            user.name,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            user.email ?: "",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                        
                        // optional list footer: small loader when state is loading (e.g., pagination/refresh)
                        item {
                            if (uiState is UsersUiState.Loading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    
                }
            }
        }
    }
}

