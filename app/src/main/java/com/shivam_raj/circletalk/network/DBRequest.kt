package com.shivam_raj.circletalk.network

import com.shivam_raj.circletalk.util.User
import io.appwrite.Query
import io.appwrite.extensions.tryJsonCast
import io.appwrite.services.Databases
import javax.inject.Inject

class DBRequest @Inject constructor(
    private val database: Databases
) {
    suspend fun getAllUser(): List<User> {
        return database.listDocuments(
            databaseId = "circle_talk_db",
            collectionId = "users",
            queries = listOf(
                Query.limit(15)
            )
        ).documents.map {
            it.tryJsonCast(User::class.java) ?: User.GuestUser
        }
    }
}