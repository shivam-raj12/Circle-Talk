package com.shivam_raj.circletalk.screens.chat.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivam_raj.circletalk.network.DBRequest
import com.shivam_raj.circletalk.util.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val dbRequest: DBRequest
) : ViewModel() {
    private val _userListState = MutableStateFlow<List<User>>(emptyList())
    val userListState = _userListState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadRecommendation()
    }

    fun loadRecommendation() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _userListState.value = dbRequest.getAllUser()
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }
}