package com.anz.feature.users.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.anz.core.model.User
import com.anz.feature.users.presentation.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(user: User, vm: UsersViewModel) {
    val scrollState = rememberScrollState()
    val clipboard: ClipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top row with back button and name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { vm.backToList() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            
            Spacer(modifier = Modifier.width(4.dp))
            
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Photo with loader & error handling
        val painter = rememberAsyncImagePainter(model = user.photo)
        val painterState = painter.state
        
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // The image (will be empty if model == null)
                androidx.compose.foundation.Image(
                    painter = painter,
                    contentDescription = "${user.name} photo",
                    modifier = Modifier
                        .fillMaxSize()
                )
                
                // Loading indicator while image loads
                if (painterState is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                }
                
                // Error placeholder
                if (painterState is AsyncImagePainter.State.Error || user.photo.isNullOrBlank()) {
                    // show a simple avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.name.firstOrNull()?.uppercase() ?: "?",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Helper to show rows
        @Composable
        fun InfoRow(icon: ImageVector, label: String, value: String?, onValueClick: (() -> Unit)? = null) {
            if (value.isNullOrBlank()) return
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .then(
                        if (onValueClick != null) Modifier.clickable { onValueClick() } else Modifier
                    )
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = value, style = MaterialTheme.typography.bodyLarge)
                }
                if (onValueClick != null) {
                    Icon(Icons.Default.Edit, contentDescription = "Copy", modifier = Modifier.size(20.dp))
                }
            }
            Divider()
        }
        
        // Company
        InfoRow(Icons.Default.AccountBox, "Company", user.company) {
            clipboard.setText(AnnotatedString(user.company ?: ""))
        }
        
        // Username
        InfoRow(Icons.Default.Person, "Username", user.username) {
            clipboard.setText(AnnotatedString(user.username ?: ""))
        }
        
        // Email (tap to open mail client)
        InfoRow(Icons.Default.Email, "Email", user.email) {
            clipboard.setText(AnnotatedString(user.email ?: ""))
        }
        
        // Phone (tap to dial)
        InfoRow(Icons.Default.Phone, "Phone", user.phone) {
            val phone = user.phone ?: return@InfoRow
            // attempt to launch dialer
            uriHandler.openUri("tel:$phone")
        }
        
        // Address / location combined
        val addressParts = listOfNotNull(user.address, user.zip, user.state, user.country).joinToString(", ")
        InfoRow(Icons.Default.LocationOn, "Address", addressParts.ifBlank { null }) {
            clipboard.setText(AnnotatedString(addressParts))
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Action buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { vm.backToList() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back")
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}
