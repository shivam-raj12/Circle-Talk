package com.shivam_raj.circletalk.util

data class User(
    val name: String,
    val username: String,
    val email: String,
    val profile: String
) {
    companion object {
        val GuestUser = User("Guest", "Guest", "Guest", "")
    }
}