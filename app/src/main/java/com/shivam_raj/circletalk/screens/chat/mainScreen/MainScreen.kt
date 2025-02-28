package com.shivam_raj.circletalk.screens.chat.mainScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shivam_raj.circletalk.navigation.Auth
import com.shivam_raj.circletalk.navigation.MainDestinations
import com.shivam_raj.circletalk.notification.NotificationPermission
import com.shivam_raj.circletalk.screens.UserProfile
import com.shivam_raj.circletalk.util.User

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainScreenTopBar{
            navController.navigate(Auth){
                popUpTo(MainDestinations.MainScreen) {
                    inclusive = true
                }
            }
        }
        if (viewModel.isLoading.collectAsStateWithLifecycle().value) {
            CircularProgressIndicator()
        } else {
            UserList(viewModel.userListState.collectAsStateWithLifecycle().value){
                navController.navigate(MainDestinations.ChatScreen(
                    userId = it.userId,
                    userName = it.name,
                    userProfile = it.profile
                ))
            }
        }
    }
    NotificationPermission()
}

@Composable
fun UserList(
    list: List<User>,
    onUserClicked: (User) -> Unit
) {
    LazyColumn {
        items(list) {
            UserCard(it, onUserClicked)
        }
    }
}

@Composable
fun UserCard(
    user: User,
    onUserClicked:(User)-> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onUserClicked(user)
            }
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        UserProfile(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            name = user.name,
            profileUri = user.profile
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}