package com.shivam_raj.circletalk.server

import android.content.Context
import com.shivam_raj.circletalk.util.User
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Avatars
import io.appwrite.services.Databases

object Server {
    @Volatile
    private lateinit var client: Client
    private var user: User? = null
    private val LOCK = Any()

    fun initializeClient(context: Context) {
        synchronized(LOCK) {
            if (!::client.isInitialized) {
                client = Client(context.applicationContext)
                    .setEndpoint("https://cloud.appwrite.io/v1")
                    .setProject("676f6c780038d69689d8")
                    .setSelfSigned(true)
            }
        }
    }

    fun initializeUser(user: User?) {
        this.user = user
    }

    fun getUser() = user

    fun getAccountInstance() = Account(client)

    fun getDatabaseInstance() = Databases(client)

    fun getAvatarInstance() = Avatars(client)
}