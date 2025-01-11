package com.shivam_raj.circletalk.screens.chat.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shivam_raj.circletalk.notification.NotificationPermission
import com.shivam_raj.circletalk.server.Server
import com.shivam_raj.circletalk.util.User

@Composable
@Preview(showBackground = true)
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainScreenTopBar()
        SearchField()
        if (viewModel.isLoading.collectAsStateWithLifecycle().value) {
            CircularProgressIndicator()
        } else {
            UserList(viewModel.userListState.collectAsStateWithLifecycle().value)
        }
    }
    NotificationPermission()
}

@Composable
fun UserList(list: List<User>) {
    LazyColumn {
        items(list) {
            UserCard(it)
        }
    }
}

@Composable
fun UserCard(user: User) {
    var profile by remember {
        mutableStateOf<Bitmap?>(null)
    }
    LaunchedEffect(Unit) {
        val avatar = Server.getAvatarInstance().getInitials(user.name, 50, 50)
        profile = BitmapFactory.decodeByteArray(avatar, 0, avatar.size)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        profile?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } ?: CircularProgressIndicator()
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = user.username,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}