package com.shivam_raj.circletalk.network

import android.content.Context
import com.shivam_raj.circletalk.storage.CurrentUserManager
import com.shivam_raj.circletalk.storage.FCMTokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRequest @Inject constructor(
    @ApplicationContext private val context: Context,
    private val account: Account
) {
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
            loginToAccount(email, password)
            return null
        } catch (e: AppwriteException) {
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

    suspend fun createPushTarget() {
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
}