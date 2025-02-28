package com.shivam_raj.circletalk.screens.chat.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivam_raj.circletalk.server.Server
import com.shivam_raj.circletalk.util.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.Query
import io.appwrite.extensions.tryJsonCast
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val database: Databases,
) : ViewModel() {
    private val _userListState = MutableStateFlow<List<User>>(emptyList())
    val userListState = _userListState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            if (_userListState.value.isEmpty()) {
                _isLoading.value = true
                try {
                    val users = loadRecommendation()
                    _userListState.value = users
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error loading recommendations", e)
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    suspend fun loadRecommendation(): List<User> {
        return database.listDocuments(
            databaseId = "circle_talk_db",
            collectionId = "users",
            queries = listOf(
                Query.notEqual("userId", Server.getUser()?.id ?: ""),
                Query.limit(15)
            )
        ).documents.map { it.data.tryJsonCast(User::class.java) ?: User.GuestUser }
    }
}