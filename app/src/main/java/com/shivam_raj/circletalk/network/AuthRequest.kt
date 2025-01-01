package com.shivam_raj.circletalk.network

import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import javax.inject.Inject

class AuthRequest @Inject constructor(client: Client) {

    private val account: Account = Account(client)

    suspend fun createUserAccount(
        email: String,
        password: String,
        username: String,
        name: String
    ): String? {
        try {
            account.create(
                userId = username,
                email = email,
                password = password,
                name = name
            )
            return loginToAccount(email, password)
        } catch (e: AppwriteException) {
            return e.message
        }
    }

    suspend fun loginToAccount(
        email: String,
        password: String,
    ): String? {
        try {
            account.createEmailPasswordSession(
                email = email,
                password = password,
            )
            return null
        } catch (e: AppwriteException) {
            return e.message
        }
    }
}