package com.shivam_raj.circletalk.network

import android.content.Context
import com.shivam_raj.circletalk.storage.CurrentUserManager
import com.shivam_raj.circletalk.storage.FCMTokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRequest @Inject constructor(
    @ApplicationContext private val context: Context,
    private val account: Account,
    private val database: Databases
) {
    suspend fun createUserAccount(
        email: String,
        password: String,
        username: String,
        name: String
    ): String? {
        try {
            database.getDocument(
                databaseId = "circle_talk_db",
                collectionId = "users",
                documentId = username
            )
            return "Username already exists"
        } catch (e: AppwriteException) {
            if (e.type == "document_not_found") {
                return createAccount(email, password, username, name)
            }
            return e.message
        }
    }

    suspend fun loginToAccount(
        email: String,
        password: String,
    ): String? {
        try {
            val user = account.createEmailPasswordSession(
                email = email,
                password = password
            )
            CurrentUserManager.storeCurrentUserId(context, user.userId)
            createPushTarget()
            return null
        } catch (e: AppwriteException) {
            return e.message
        }
    }

    private suspend fun createPushTarget() {
        try {
            val token = FCMTokenManager.getToken(context)
            val target = FCMTokenManager.getTargetId(context)
            combine(token, target) { tokenValue, targetValue ->
                if (tokenValue != null && targetValue == null) {
                    withContext(Dispatchers.IO) {
                        val target = account.createPushTarget(ID.unique(), tokenValue)
                        FCMTokenManager.storeTarget(context, target.id)
                    }
                }
            }.take(1).collect()
        } catch (_: Exception) {

        }
    }

    private suspend inline fun createAccount(
        email: String,
        password: String,
        username: String,
        name: String
    ): String? {
        try {
            val user = account.create(
                userId = ID.unique(),
                email = email,
                password = password,
                name = name
            )
            return createUserDatabase(user, username)
        } catch (e: AppwriteException) {
            return e.message
        }
    }

    private suspend fun createUserDatabase(
        user: User<Map<String, Any>>,
        username: String
    ): String? {
        try {
            database.createDocument(
                databaseId = "circle_talk_db",
                collectionId = "users",
                documentId = username,
                data = mapOf(
                    "userId" to user.id,
                    "name" to user.name,
                    "email" to user.email,
                    "username" to username,
                    "createdAt" to user.createdAt
                )
            )
            return null
        } catch (e: AppwriteException) {
            return e.message
        }
    }
}