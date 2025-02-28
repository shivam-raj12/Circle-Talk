package com.shivam_raj.circletalk.screens.chat.chatScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.shivam_raj.circletalk.screens.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    userId: String,
    userName: String,
    userProfile: String?,
    viewModel: ChatScreenViewModel = hiltViewModel()
) {
    var message by remember { mutableStateOf("") }
    LaunchedEffect(userId) {
        viewModel.loadMessages(userId)
    }
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(text = userName)
            },
            navigationIcon = {
                Row {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    UserProfile(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        name = userName,
                        profileUri = userProfile
                    )
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(viewModel.messages.value) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (it.senderId == userId) Alignment.CenterStart else Alignment.CenterEnd
                ) {
                    Column {
                        Text(text = it.time)
                        Text(text = it.message, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
        TextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Type a message") },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.sendMessage(userId, message)
                    message = ""
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Default.Send, contentDescription = "Send")
                }
            }
        )
    }
}