package com.shivam_raj.circletalk.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.shivam_raj.circletalk.R

@Composable
fun UserProfile(
    modifier: Modifier,
    name: String, // Required if profileUri is null
    profileUri: String?
) {
    AsyncImage(
        modifier = modifier,
        model = profileUri ?: "https://cloud.appwrite.io/v1/avatars/initials?name={ $name }&width=100&height=100",
        contentDescription = "User profile",
        placeholder = painterResource(R.drawable.profile)
    )

}